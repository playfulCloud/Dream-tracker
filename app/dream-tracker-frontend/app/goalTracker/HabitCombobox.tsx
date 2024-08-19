import * as React from "react";
import { Check, ChevronsUpDown } from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
} from "@/components/ui/command";
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover";

export function HabitCombobox({ habits, value, onChange }) {
    const [open, setOpen] = React.useState(false);

    return (
        <Popover open={open} onOpenChange={setOpen}>
            <PopoverTrigger asChild>
                <Button
                    variant="outline"
                    role="combobox"
                    aria-expanded={open}
                    className="w-[280px] justify-between"
                >
                    {value
                        ? habits.find((habit) => habit.id === value)?.name
                        : "Select a habit"}
                    <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
            </PopoverTrigger>
            <PopoverContent className="w-[200px] p-0">
                <Command>
                    <CommandInput placeholder="Search habits..." />
                    <CommandList>
                        <CommandEmpty>No habit found.</CommandEmpty>
                        <CommandGroup>
                            {habits.map((habit) => (
                                <CommandItem
                                    key={habit.id}
                                    value={habit.id}
                                    onSelect={(currentValue) => {
                                        const selectedValue = currentValue === value ? "" : currentValue;
                                        onChange(selectedValue);
                                        setOpen(false);
                                    }}
                                >
                                    <Check
                                        className={cn(
                                            "mr-2 h-4 w-4",
                                            value === habit.id ? "opacity-100" : "opacity-0"
                                        )}
                                    />
                                    {habit.name}
                                </CommandItem>
                            ))}
                        </CommandGroup>
                    </CommandList>
                </Command>
            </PopoverContent>
        </Popover>
    );
}
