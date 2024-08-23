"use client";

import * as React from "react";
import { Bar, BarChart, CartesianGrid, XAxis, YAxis } from "recharts";
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {
    ChartConfig,
    ChartContainer,
    ChartTooltip,
    ChartTooltipContent,
} from "@/components/ui/chart";
import { useAppContext } from '../AppContext';

export const description = "An interactive bar chart showing the number of habits completed each day relative to the total habits over the next 30 days.";

const chartConfig = {
    doneHabits: {
        label: "Done Habits",
        color: "hsl(var(--chart-1))",
    },
} satisfies ChartConfig;

export function HabitChart() {
    const { chartData, fetchCharts } = useAppContext();

    // Calculate dates for the next 30 days including today
    const currentDate = new Date();
    const next30Days = Array.from({ length: 30 }, (_, i) => {
        const date = new Date();
        date.setDate(currentDate.getDate() + i);
        return date.toISOString().split('T')[0];  // Format as YYYY-MM-DD
    });


    const filteredData = next30Days.map(date => {
        const dayData = chartData.find(item => item.date === date);
        return {
            date,
            count: dayData ? dayData.count : 0,
            all: dayData ? dayData.all : 0,
        };
    });

    React.useEffect(() => {
        fetchCharts();
    }, [fetchCharts]);

    const totalAllHabits = React.useMemo(
        () => Math.max(...filteredData.map(item => item.all), 0),  // Get the maximum total habits across all days
        [filteredData]
    );

    return (
        <div className="container mx-auto p-4 max-w-3xl">
            <Card>
                <CardHeader className="flex flex-col items-stretch space-y-0 border-b p-0 sm:flex-row">
                    <div className="flex flex-1 flex-col justify-center gap-1 px-6 py-5 sm:py-6">
                        <CardTitle>Habits Chart </CardTitle>
                        <CardDescription>
                            Showing total habits completed relative to total habits for the next 30 days
                        </CardDescription>
                    </div>
                </CardHeader>
                <CardContent className="px-2 sm:p-6">
                    <ChartContainer
                        className="aspect-auto h-[250px] w-full"
                        config={chartConfig} // Passing the config to the ChartContainer
                    >
                        <BarChart
                            accessibilityLayer
                            data={filteredData}
                            margin={{
                                left: 12,
                                right: 12,
                            }}
                        >
                            <CartesianGrid vertical={false} />
                            <XAxis
                                dataKey="date"
                                tickLine={false}
                                axisLine={false}
                                tickMargin={8}
                                minTickGap={32}
                                tickFormatter={(value) => {
                                    const date = new Date(value);
                                    return date.toLocaleDateString("en-US", {
                                        month: "short",
                                        day: "numeric",
                                    });
                                }}
                            />
                            <YAxis domain={[0, totalAllHabits]} /> {/* Set Y-axis maximum to totalAllHabits */}
                            <ChartTooltip
                                content={
                                    <ChartTooltipContent
                                        className="w-[150px]"
                                        nameKey={chartConfig.doneHabits.label} // Using label from config
                                        labelFormatter={(value) => {
                                            return new Date(value).toLocaleDateString("en-US", {
                                                month: "short",
                                                day: "numeric",
                                                year: "numeric",
                                            });
                                        }}
                                    />
                                }
                            />
                            <Bar dataKey="count" name={chartConfig.doneHabits.label} fill={chartConfig.doneHabits.color} /> {/* Using color from config */}
                        </BarChart>
                    </ChartContainer>
                </CardContent>
            </Card>
        </div>
    );
}

export default HabitChart;
