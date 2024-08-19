"use client";

import * as React from "react";
import { DateTime, Duration } from "luxon";
import {
    ColumnDef,
    ColumnFiltersState,
    SortingState,
    VisibilityState,
    flexRender,
    getCoreRowModel,
    getFilteredRowModel,
    getPaginationRowModel,
    getSortedRowModel,
    useReactTable,
} from "@tanstack/react-table";
import { ChevronDown, MoreHorizontal, Trash } from "lucide-react";
import { ProgressDemo } from "@/app/goalTracker/goalProgress";

import { Button } from "@/components/ui/button";
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
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { useAppContext } from '../AppContext';
import { GoalCreateForm } from "@/app/goalTracker/goalCreateForm";
import axios from "axios";

export type Goal = {
    id: string;
    name: string;
    duration: string;
    habitID: string;
    completionCount: number;
    currentCount: number;
    status: string;
    createdAt: string;
};

const calculateTimeLeft = (createdAt: string, duration: string) => {
    try {
        const createdDate = DateTime.fromISO(createdAt);
        const durationObj = Duration.fromISO(duration);

        if (durationObj.isValid) {
            const endDate = createdDate.plus(durationObj);
            const now = DateTime.now();
            const timeLeft = endDate.diff(now, ['days', 'hours']);

            if (timeLeft.toMillis() <= 0) {
                return "Time's up";
            }

            const daysLeft = timeLeft.days;
            const hoursLeft = timeLeft.hours;

            return `${Math.floor(daysLeft)} days, ${Math.floor(hoursLeft)} hours`;
        } else {
            console.error("Invalid duration format:", duration);
            return "Invalid duration";
        }
    } catch (error) {
        console.error("Error calculating time left:", error);
        return "Error in calculation";
    }
};

export function GoalTable() {
    const { goals, loading, error, fetchGoals } = useAppContext();
    const [sorting, setSorting] = React.useState<SortingState>([]);
    const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([]);
    const [columnVisibility, setColumnVisibility] =
        React.useState<VisibilityState>({});
    const [rowSelection, setRowSelection] = React.useState({});

    const handleDeleteGoal = async (id: string) => {
        try {
            const token = localStorage.getItem('token');
            await axios.delete(`http://localhost:8080/v1/goals/${id}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            fetchGoals();
        } catch (error) {
            console.error('Failed to delete goal', error);
        }
    };

    const columns: ColumnDef<Goal>[] = [
        {
            accessorKey: "name",
            header: "Goal Name",
            cell: ({ row }) => (
                <div className="capitalize">{row.getValue("name")}</div>
            ),
        },
        {
            accessorKey: "timeLeft",
            header: "Time Left",
            cell: ({ row }) => (
                <div>
                    {calculateTimeLeft(row.original.createdAt, row.original.duration)}
                </div>
            ),
        },
        {
            accessorKey: "status",
            header: "Status",
            cell: ({ row }) => (
                <div className="capitalize">
                    {row.getValue("status")}
                </div>
            ),
        },
        {
            accessorKey: "completionCount",
            header: ({ column }) => {
                return (
                    <Button
                        variant="ghost"
                        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
                    >
                        Completion
                    </Button>
                );
            },
            cell: ({ row }) => (
                <div className="lowercase">
                    <ProgressDemo
                        completionCount={row.original.completionCount}
                        currentCount={row.original.currentCount}
                    />
                </div>
            ),
        },
        {
            accessorKey: "count",
            header: "Count",
            cell: ({ row }) => (
                <div className="capitalize">{row.original.currentCount}/{row.getValue("completionCount")}</div>
            ),
        },
        {
            id: "actions",
            enableHiding: false,
            cell: ({ row }) => {
                const goal = row.original;

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

                            <DropdownMenuSeparator />
                            <DropdownMenuItem className="text-red-600"
                                              onClick={() => handleDeleteGoal(goal.id)}
                            >
                                <Trash className="mr-2 h-4 w-4" />
                                Delete Goal
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                );
            },
        },

    ];

    const table = useReactTable({
        data: goals,
        columns,
        onSortingChange: setSorting,
        onColumnFiltersChange: setColumnFilters,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
        getSortedRowModel: getSortedRowModel(),
        getFilteredRowModel: getFilteredRowModel(),
        onColumnVisibilityChange: setColumnVisibility,
        onRowSelectionChange: setRowSelection,
        state: {
            sorting,
            columnFilters,
            columnVisibility,
            rowSelection,
        },
    });

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div className="container mx-auto p-4 max-w-3xl">
            <div className="flex items-center space-x-4 py-4">
                <Input
                    placeholder="Filter goals..."
                    value={(table.getColumn("name")?.getFilterValue() as string) ?? ""}
                    onChange={(event) =>
                        table.getColumn("name")?.setFilterValue(event.target.value)
                    }
                    className="max-w-sm"
                />
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button variant="outline">
                            Columns <ChevronDown className="ml-2 h-4 w-4" />
                        </Button>
                    </DropdownMenuTrigger>
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
                <GoalCreateForm />
            </div>
            <div className="rounded-md border">
                <Table>
                    <TableHeader>
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
                                const status = row.original.status;
                                const rowColor =
                                    status === "FAILED" ? "bg-red-100" :
                                        status === "DONE" ? "bg-green-100" :
                                            "";

                                return (
                                    <TableRow
                                        key={row.id}
                                        data-state={row.getIsSelected() && "selected"}
                                        className={rowColor}
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
        </div>
    );
}

export default GoalTable;
