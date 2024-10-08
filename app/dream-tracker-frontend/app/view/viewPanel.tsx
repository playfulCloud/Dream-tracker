"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel } from "@/components/ui/form";
import { Switch } from "@/components/ui/switch";
import { useEffect } from "react";
import axios from 'axios';
import {toast} from "@/components/ui/use-toast";

const FormSchema = z.object({
    habits: z.boolean().default(true),
    goals: z.boolean().default(true),
    statistics: z.boolean().default(true),
    chart: z.boolean().default(true),
});

const getToken = (): string | null => {
    return localStorage.getItem('token');
};

export default function ViewManager() {
    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
        defaultValues: {
            habits: JSON.parse(localStorage.getItem("views") || "[]").includes("habits"),
            goals: JSON.parse(localStorage.getItem("views") || "[]").includes("goals"),
            statistics: JSON.parse(localStorage.getItem("views") || "[]").includes("statistics"),
            chart: JSON.parse(localStorage.getItem("views") || "[]").includes("chart"),
        },
    });

    useEffect(() => {
        const storedViews = JSON.parse(localStorage.getItem("views") || "[]");
        form.reset({
            habits: storedViews.includes("habits"),
            goals: storedViews.includes("goals"),
            statistics: storedViews.includes("statistics"),
            chart: storedViews.includes("chart"),
        });
    }, [form]);

    const onSubmit = async (data) => {
        const selectedViews = Object.keys(data).filter((key) => data[key]);
        localStorage.setItem("views", JSON.stringify(selectedViews));

        try {
            const token = getToken();
            await axios.put('http://localhost:8080/v1/position-activation', {
                habitEnabled: selectedViews.includes('habits'),
                goalEnabled: selectedViews.includes('goals'),
                statsEnabled: selectedViews.includes('statistics'),
                chartsEnabled: selectedViews.includes('chart')
            }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            toast({
                title: "View updated",
            })
        } catch (error) {
            console.error('Error saving views:', error);
        }
    };

    return (
        <div className="container mx-auto p-4 max-w-3xl">
            <Card className="p-6">
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="w-full space-y-6">
                        <div>
                            <h3 className="mb-4 text-lg font-medium">Main View Manager</h3>
                            <div className="space-y-4">
                                <FormField
                                    control={form.control}
                                    name="habits"
                                    render={({ field }) => (
                                        <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                                            <div className="space-y-0.5">
                                                <FormLabel className="text-base">Habit Tracker</FormLabel>
                                                <FormDescription>View panel which displays user habits and allows to create new and track them.</FormDescription>
                                            </div>
                                            <FormControl>
                                                <Switch checked={field.value} onCheckedChange={field.onChange} />
                                            </FormControl>
                                        </FormItem>
                                    )}
                                />
                                <FormField
                                    control={form.control}
                                    name="goals"
                                    render={({ field }) => (
                                        <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                                            <div className="space-y-0.5">
                                                <FormLabel className="text-base">Goal Tracker</FormLabel>
                                                <FormDescription>View panel which displays user goals and allows to create and follow their progress.</FormDescription>
                                            </div>
                                            <FormControl>
                                                <Switch checked={field.value} onCheckedChange={field.onChange} />
                                            </FormControl>
                                        </FormItem>
                                    )}
                                />
                                <FormField
                                    control={form.control}
                                    name="statistics"
                                    render={({ field }) => (
                                        <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                                            <div className="space-y-0.5">
                                                <FormLabel className="text-base">Habit Statistics</FormLabel>
                                                <FormDescription>View panel which displays statistics based on habit completion.</FormDescription>
                                            </div>
                                            <FormControl>
                                                <Switch checked={field.value} onCheckedChange={field.onChange} />
                                            </FormControl>
                                        </FormItem>
                                    )}
                                />
                                <FormField
                                    control={form.control}
                                    name="chart"
                                    render={({ field }) => (
                                        <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                                            <div className="space-y-0.5">
                                                <FormLabel className="text-base">Habits Chart</FormLabel>
                                                <FormDescription>View panel which displays the number of habits done during a day.</FormDescription>
                                            </div>
                                            <FormControl>
                                                <Switch checked={field.value} onCheckedChange={field.onChange} />
                                            </FormControl>
                                        </FormItem>
                                    )}
                                />
                            </div>
                        </div>
                        <Button type="submit">Submit</Button>
                    </form>
                </Form>
            </Card>
        </div>
    );
}
