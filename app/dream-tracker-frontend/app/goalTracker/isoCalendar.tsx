"use client";

import * as React from "react";
import { DateTime } from "luxon";
import { Calendar } from "@/components/ui/calendar";

export function CalendarForGoals({ onDateSelect }) {
    const [date, setDate] = React.useState<Date | undefined>(new Date());

    const calculateISO8601Period = (selectedDate: Date | undefined): string | null => {
        if (!selectedDate) return null;

        const now = DateTime.now(); // Obecny dzień
        const endDate = DateTime.fromJSDate(selectedDate); // Zaznaczona data

        if (endDate < now) {
            return "Selected date is in the past. Cannot calculate period.";
        }

        const duration = endDate.diff(now, ['days', 'hours', 'minutes']).toISO();
        return duration;
    };

    const handleDateSelect = (selectedDate: Date | undefined) => {
        setDate(selectedDate);
        const isoPeriod = calculateISO8601Period(selectedDate);
        if (isoPeriod) {
            onDateSelect(isoPeriod); // Przekazanie okresu ISO 8601 do formularza
        }
    };

    return (
        <Calendar
            mode="single"
            selected={date}
            onSelect={handleDateSelect}
            className="rounded-md border"
        />
    );
}
