"use client"
import React, { useEffect, useState, useRef } from "react";
import Draggable from "react-draggable";
import HabitTracker from "@/app/habitTracker/habitTracker";
import GoalTable from "@/app/goalTracker/goalTracker";
import Dashboard from "@/app/stats/stats";
import HabitChart from "@/app/habitChart/page";

export default function MainPanel() {
    const [views, setViews] = useState<string[]>([]);
    const habitRef = useRef(null);
    const goalRef = useRef(null);
    const dashboardRef = useRef(null);
    const chartRef = useRef(null);

    useEffect(() => {
        const storedViews = JSON.parse(localStorage.getItem("views") || "[]");
        setViews(storedViews);
    }, []);

    const resetPositions = () => {
        // Reset the position of all draggable elements
        if (habitRef.current) habitRef.current.state.x = 0;
        if (habitRef.current) habitRef.current.state.y = 0;
        if (goalRef.current) goalRef.current.state.x = 0;
        if (goalRef.current) goalRef.current.state.y = 0;
        if (dashboardRef.current) dashboardRef.current.state.x = 0;
        if (dashboardRef.current) dashboardRef.current.state.y = 0;
        if (chartRef.current) chartRef.current.state.x = 0;
        if (chartRef.current) chartRef.current.state.y = 0;
        // Force re-render to apply the reset positions
        setViews([...views]);
    };

    return (
        <div className="relative h-screen">
            <button
                onClick={resetPositions}
                className="absolute top-4 right-4 bg-blue-500 text-white p-2 rounded"
            >
                Reset Positions
            </button>
            {views.includes("habits") && (
                <Draggable ref={habitRef}>
                    <div className="absolute bg-white shadow-md flex items-center justify-center p-4 min-w-[300px] min-h-[200px]">
                        <HabitTracker />
                    </div>
                </Draggable>
            )}
            {views.includes("goals") && (
                <Draggable ref={goalRef}>
                    <div className="absolute bg-white shadow-md flex items-center justify-center p-4 min-w-[300px] min-h-[200px]">
                        <GoalTable />
                    </div>
                </Draggable>
            )}
            {views.includes("statistics") && (
                <Draggable ref={dashboardRef}>
                    <div className="absolute bg-white shadow-md flex items-center justify-center p-4 min-w-[300px] min-h-[200px]">
                        <Dashboard />
                    </div>
                </Draggable>
            )}
            {views.includes("chart") && (
                <Draggable ref={chartRef}>
                    <div className="absolute bg-white shadow-md flex items-center justify-center p-4 min-w-[300px] min-h-[200px]">
                        <HabitChart />
                    </div>
                </Draggable>
            )}
        </div>
    );
}
