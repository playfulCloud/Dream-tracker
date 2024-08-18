"use client";
import * as React from "react";
import { useEffect, useState } from "react";
import { useAppContext } from '../AppContext';
import { CreateForm } from "@/app/habitTracker/createForm";
import { UpdateForm } from "@/app/habitTracker/updateForm";
import { ArrowUpDown, ChevronDown, MoreHorizontal, Trash } from "lucide-react";
import axios from 'axios';
import {
    ColumnDef,
    ColumnFiltersState,
    flexRender,
    getCoreRowModel,
    getFilteredRowModel,
    getPaginationRowModel,
    getSortedRowModel,
    SortingState,
    useReactTable,
    VisibilityState,
} from "@tanstack/react-table";
import { differenceInDays, differenceInMonths, differenceInWeeks, formatDistanceToNowStrict, isFuture } from "date-fns";

import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import {
    DropdownMenu,
    DropdownMenuCheckboxItem,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Input } from "@/components/ui/input";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow, } from "@/components/ui/table";

export type Habit = {
    id: string;
    name: string;
    action: string;
    duration: string;
    difficulty: string;
    cooldownTill: string;
    frequency: string;
    status: string;
};

interface HabitTrackResponse {
    date: string;
    status: string;
}

export const createColumns = (
    handleCheckboxChange: (rowId: string, checked: boolean) => void,
    handleDelete: (id: string) => void,
    cooldownTimers: { [key: string]: string },
    openUpdateForm: (habit: Habit) => void,
    rowSelection: { [key: string]: boolean }
): ColumnDef<Habit>[] => [
    {
        id: "select",
        cell: ({ row }) => {
            const isDisabled = isFuture(new Date(row.original.cooldownTill));
            return (
                <Checkbox
                    checked={!!rowSelection[row.original.id]}
                    onCheckedChange={(value) => handleCheckboxChange(row.original.id, !!value)}
                    aria-label="Select row"
                    disabled={isDisabled}
                />
            );
        },
        enableSorting: false,
        enableHiding: false,
    },
    {
        accessorKey: "name",
        header: "Habit Name",
        cell: ({ row }) => (
            <div className="capitalize">{row.getValue("name")}</div>
        ),
    },
    {
        accessorKey: "cooldownTill",
        header: "Cooldown",
        cell: ({ row }) => {
            const cooldownTime = cooldownTimers[row.original.id];
            return (
                <div className="capitalize">
                    {cooldownTime || "Ready"}
                </div>
            );
        },
    },
    {
        accessorKey: "action",
        header: ({ column }) => (
            <Button
                variant="ghost"
                onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
            >
                Action
                <ArrowUpDown className="ml-2 h-4 w-4" />
            </Button>
        ),
        cell: ({ row }) => <div className="capitalize">{row.getValue("action")}</div>,
    },
    {
        accessorKey: "difficulty",
        header: "Difficulty",
        cell: ({ row }) => (
            <div className="capitalize">{row.getValue("difficulty")}</div>
        ),
    },
    {
        accessorKey: "frequency",
        header: "Frequency",
        cell: ({ row }) => (
            <div className="capitalize">{row.getValue("frequency")}</div>
        ),
    },
    {
        id: "actions",
        enableHiding: false,
        cell: ({ row }) => {
            const habit = row.original;

            return (
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button variant="ghost" className="h-8 w-8 p-0">
                            <span className="sr-only">Open menu</span>
                            <MoreHorizontal className="h-4 w-4" />
                        </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                        <DropdownMenuLabel>Actions</DropdownMenuLabel>
                        <DropdownMenuItem
                            onClick={() => openUpdateForm(habit)}
                        >
                            Update habit

                        </DropdownMenuItem>
                        <DropdownMenuSeparator />
                        <DropdownMenuItem className="text-red-600"
                                          onClick={() => handleDelete(habit.id)}
                        >
                            <Trash className="mr-2 h-4 w-4" />
                            Delete habit
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            );
        },
    },
];

const getToken = (): string | null => {
    return localStorage.getItem('token');
};

export function HabitTracker() {
    const [cooldownTimers, setCooldownTimers] = useState<{ [key: string]: string }>({});
    const [isOpen, setIsOpen] = useState(false);
    const [currentHabit, setCurrentHabit] = useState<Habit | null>(null);
    const [rowSelection, setRowSelection] = useState<{ [key: string]: boolean }>({});

    const handleCheckboxChange = (rowId: string, checked: boolean) => {
        if (checked) {
            setRowSelection((prev) => ({
                ...prev,
                [rowId]: true,
            }));
            handleTracking(rowId);
        } else {
            setRowSelection((prev) => {
                const updatedSelection = { ...prev };
                delete updatedSelection[rowId];
                return updatedSelection;
            });
        }
    };

    const handleTracking = async (id: string) => {
        try {
            const token = getToken();
            const habitTrackingRequest = { habitId: id, status: "DONE" };
            await axios.post<HabitTrackResponse>('http://localhost:8080/v1/habits-tracking', habitTrackingRequest, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            fetchHabits();
            fetchGoals();
        } catch (error) {
            console.error('Failed to track habit', error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            const token = getToken();
            await axios.delete(`http://localhost:8080/v1/habits/${id}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            fetchHabits();
        } catch (error) {
            console.error('Failed to delete habit', error);
        }
    };

    const openUpdateForm = (habit: Habit) => {
        setCurrentHabit(habit);
        setIsOpen(true);
    };

    const closeUpdateForm = () => {
        setIsOpen(false);
        setCurrentHabit(null);
    };

    const { habits, fetchHabits, fetchGoals } = useAppContext();
    const [sorting, setSorting] = React.useState<SortingState>([]);
    const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([]);
    const [columnVisibility, setColumnVisibility] =
        React.useState<VisibilityState>({});
    const columns = createColumns(handleCheckboxChange, handleDelete, cooldownTimers, openUpdateForm, rowSelection);
    useEffect(() => {
        const updateCooldowns = () => {
            const updatedTimers: { [key: string]: string } = {};
            habits.forEach((habit) => {
                const cooldownDate = new Date(habit.cooldownTill);
                const currentDate = new Date();

                if (isFuture(cooldownDate)) {
                    const daysDifference = differenceInDays(cooldownDate, currentDate);
                    const weeksDifference = differenceInWeeks(cooldownDate, currentDate);
                    const monthsDifference = differenceInMonths(cooldownDate, currentDate);

                    if (daysDifference === 1) {
                        updatedTimers[habit.id] = "Comeback Tomorrow";
                    } else if (weeksDifference === 1) {
                        updatedTimers[habit.id] = "Comeback Next Week";
                    } else if (monthsDifference === 1) {
                        updatedTimers[habit.id] = "Comeback Next Month";
                    } else {
                        updatedTimers[habit.id] = formatDistanceToNowStrict(cooldownDate);
                    }
                } else {
                    updatedTimers[habit.id] = "Ready";
                }
            });
            setCooldownTimers(updatedTimers);
        };

        updateCooldowns();

        const interval = setInterval(() => {
            const now = new Date();
            const nextMidnight = new Date(
                now.getFullYear(),
                now.getMonth(),
                now.getDate() + 1,
                0, 0, 0, 0
            );
            const timeToMidnight = nextMidnight.getTime() - now.getTime();
            setTimeout(updateCooldowns, timeToMidnight);
        }, 86400000);

        return () => clearInterval(interval);
    }, [habits]);

    React.useEffect(() => {
        fetchHabits();
    }, []);

    const customSort = (rowA, rowB, columnId) => {
        const isDisabledA = isFuture(new Date(rowA.original.cooldownTill));
        const isDisabledB = isFuture(new Date(rowB.original.cooldownTill));

        if (isDisabledA && !isDisabledB) return 1;
        if (!isDisabledA && isDisabledB) return -1; // Enabled rows stay at the top


        const valueA = rowA.getValue(columnId);
        const valueB = rowB.getValue(columnId);

        if (valueA > valueB) return 1;
        if (valueA < valueB) return -1;
        return 0;
    };

    const table = useReactTable({
        data: habits,
        columns,
        onSortingChange: setSorting,
        onColumnFiltersChange: setColumnFilters,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
        getSortedRowModel: getSortedRowModel({
            sortTypes: {
                custom: customSort,
            },
        }),
        getFilteredRowModel: getFilteredRowModel(),
        onColumnVisibilityChange: setColumnVisibility,
        onRowSelectionChange: setRowSelection,
        state: {
            sorting,
            columnFilters,
            columnVisibility,
            rowSelection,
        },
        autoResetPageIndex: false,
    });

    return (
        <div className="container mx-auto p-4 max-w-3xl">
            <div className="flex items-center py-4 space-x-4">
                <Input
                    placeholder="Filter habits..."
                    value={(table.getColumn("name")?.getFilterValue() as string) ?? ""}
                    onChange={(event) =>
                        table.getColumn("name")?.setFilterValue(event.target.value)
                    }
                    className="max-w-sm"
                />
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button variant="outline" className="ml-auto">
                            Columns <ChevronDown className="ml-2 h-4 w-4" />
                        </Button>
                    </DropdownMenuTrigger>
                    <CreateForm />
                    <DropdownMenuContent align="end">
                        {table
                            .getAllColumns()
                            .filter((column) => column.getCanHide())
                            .map((column) => {
                                return (
                                    <DropdownMenuCheckboxItem
                                        key={column.id}
                                        className="capitalize"
                                        checked={column.getIsVisible()}
                                        onCheckedChange={(value) =>
                                            column.toggleVisibility(!!value)
                                        }
                                    >
                                        {column.id}
                                    </DropdownMenuCheckboxItem>
                                );
                            })}
                    </DropdownMenuContent>
                </DropdownMenu>
            </div>
            <div className="rounded-md border border-gray-300 shadow-md bg-white">
                <Table>
                    <TableHeader className="bg-gray-50">
                        {table.getHeaderGroups().map((headerGroup) => (
                            <TableRow key={headerGroup.id}>
                                {headerGroup.headers.map((header) => {
                                    return (
                                        <TableHead key={header.id}>
                                            {header.isPlaceholder
                                                ? null
                                                : flexRender(
                                                    header.column.columnDef.header,
                                                    header.getContext()
                                                )}
                                        </TableHead>
                                    );
                                })}
                            </TableRow>
                        ))}
                    </TableHeader>
                    <TableBody>
                        {table.getRowModel().rows?.length ? (
                            table.getRowModel().rows.map((row) => {
                                const isDisabled = isFuture(new Date(row.original.cooldownTill));
                                return (
                                    <TableRow
                                        key={row.original.id}
                                        className={`transition-colors duration-500 ${
                                            rowSelection[row.original.id] 
                                                ? "bg-green-500 text-white"
                                                : isDisabled
                                                    ? "disabled-row" 
                                                    : "bg-white" 
                                        }`}

                                    >
                                        {row.getVisibleCells().map((cell) => (
                                            <TableCell key={cell.id}>
                                                {flexRender(
                                                    cell.column.columnDef.cell,
                                                    cell.getContext()
                                                )}
                                            </TableCell>
                                        ))}
                                    </TableRow>

                                );
                            })
                        ) : (
                            <TableRow>
                                <TableCell
                                    colSpan={columns.length}
                                    className="h-24 text-center"
                                >
                                    No results.
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </div>
            <div className="flex items-center justify-end space-x-2 py-4">
                <div className="flex-1 text-sm text-muted-foreground">
                    {table.getFilteredSelectedRowModel().rows.length} of{" "}
                    {table.getFilteredRowModel().rows.length} row(s) selected.
                </div>
                <div className="space-x-2">
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => table.previousPage()}
                        disabled={!table.getCanPreviousPage()}
                    >
                        Previous
                    </Button>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => table.nextPage()}
                        disabled={!table.getCanNextPage()}
                    >
                        Next
                    </Button>
                </div>
            </div>
            {currentHabit && (
                <UpdateForm
                    habit={currentHabit}
                    isOpen={isOpen}
                    onClose={closeUpdateForm}
                />
            )}
        </div>
    );
}

export default HabitTracker;
