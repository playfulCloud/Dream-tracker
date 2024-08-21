"use client";

import React, { useEffect, useState } from "react";
import HabitTracker from "@/app/habitTracker/page";
import GoalTable from "@/app/goalTracker/page";
import Dashboard from "@/app/stats/page";
import HabitChart from "@/app/habitChart/page";

export default function Main() {
    const [views, setViews] = useState<string[]>([]);

    useEffect(() => {
        const storedViews = JSON.parse(localStorage.getItem("views") || "[]");
        setViews(storedViews);
    }, []);

    return (
        <div className="flex flex-wrap h-screen">
            {views.includes("habits") && <HabitTracker />}
            {views.includes("goals") && <GoalTable />}
            {views.includes("statistics") && <Dashboard />}
            {views.includes("chart") && <HabitChart />}
        </div>
    );
}
