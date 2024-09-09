"use client";

import React, { useEffect, useState } from "react";
import Draggable from "react-draggable";
import HabitTracker from "@/app/habitTracker/habitTracker";
import GoalTable from "@/app/goalTracker/goalTracker";
import Dashboard from "@/app/stats/stats";
import HabitChart from "@/app/habitChart/page";
import axios from 'axios';

const getToken = (): string | null => {
    return localStorage.getItem('token');
};

export default function MainPanel() {
    const [views, setViews] = useState<string[]>([]);
    const [draggable, setDraggable] = useState(true);
    const [positions, setPositions] = useState({
        habits: { x: 0, y: 0 },
        goals: { x: 0, y: 0 },
        statistics: { x: 0, y: 0 },
        chart: { x: 0, y: 0 },
    });

    useEffect(() => {
        const fetchPositionsFromServer = async () => {
            try {
                const token = getToken();
                const response = await axios.get('http://localhost:8080/v1/positions', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                const { habitX, habitY, goalX, goalY, statX, statY, chartX, chartY, habitEnabled, goalEnabled, statsEnabled, chartsEnabled } = response.data;

                setPositions({
                    habits: { x: habitX, y: habitY },
                    goals: { x: goalX, y: goalY },
                    statistics: { x: statX, y: statY },
                    chart: { x: chartX, y: chartY }
                });

                setViews([
                    habitEnabled && 'habits',
                    goalEnabled && 'goals',
                    statsEnabled && 'statistics',
                    chartsEnabled && 'chart'
                ].filter(Boolean));
            } catch (error) {
                console.error('Error fetching positions:', error);
            }
        };

        fetchPositionsFromServer();
    }, []);

    const handleDragStop = async (e, data, view) => {
        const newPositions = {
            ...positions,
            [view]: { x: data.x, y: data.y }
        };
        setPositions(newPositions);

        try {
            const token = getToken();
            await axios.put('http://localhost:8080/v1/position-change', {
                habitX: newPositions.habits.x,
                habitY: newPositions.habits.y,
                goalX: newPositions.goals.x,
                goalY: newPositions.goals.y,
                statX: newPositions.statistics.x,
                statY: newPositions.statistics.y,
                chartX: newPositions.chart.x,
                chartY: newPositions.chart.y
            }, {
                headers: {
                    Authorization: `Bearer ${token}`, // Dodanie nagłówka z tokenem
                },
            });

        } catch (error) {
            console.error('Error saving positions:', error);
        }
    };

    const resetPositions = async () => {
        const resetPositions = {
            habits: { x: 0, y: 0 },
            goals: { x: 0, y: 0 },
            statistics: { x: 0, y: 0 },
            chart: { x: 0, y: 0 },
        };
        setPositions(resetPositions);

        try {
            const token = getToken();
            await axios.put('http://localhost:8080/v1/position-change', {
                habitX: resetPositions.habits.x,
                habitY: resetPositions.habits.y,
                goalX: resetPositions.goals.x,
                goalY: resetPositions.goals.y,
                statX: resetPositions.statistics.x,
                statY: resetPositions.statistics.y,
                chartX: resetPositions.chart.x,
                chartY: resetPositions.chart.y
            }, {
                headers: {
                    Authorization: `Bearer ${token}`, // Dodanie nagłówka z tokenem
                },
            });
        } catch (error) {
            console.error('Error resetting positions:', error);
        }
    };

    const toggleDraggable = async () => {
        setDraggable(!draggable);
        const currentViews = {
            habitEnabled: views.includes('habits'),
            goalEnabled: views.includes('goals'),
            statsEnabled: views.includes('statistics'),
            chartsEnabled: views.includes('chart'),
        };

        try {
            const token = getToken();
            await axios.put('http://localhost:8080/v1/position-activation', currentViews, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
        } catch (error) {
            console.error('Error updating view activation:', error);
        }
    };

    return (
        <div className="relative h-screen">
            <div className="absolute top-4 right-4 flex space-x-4">
                <button
                    onClick={resetPositions}
                    className="bg-blue-500 text-white p-2 rounded"
                >
                    Reset Positions
                </button>
                <button
                    onClick={toggleDraggable}
                    className={`p-2 rounded ${draggable ? 'bg-red-500' : 'bg-green-500'} text-white`}
                >
                    {draggable ? 'Lock Positions' : 'Unlock Positions'}
                </button>
            </div>
            {views.length === 0 && (
                <div className="absolute inset-0 flex items-center justify-center">
                    <p className="text-gray-200 text-3xl sm:text-4xl md:text-5xl lg:text-5xl text-center">
                        To add components, click on "View" and select the items you want to see. They can be moved around. Feel free to test it.
                    </p>
                </div>
            )}
            {views.includes("habits") && (
                <Draggable
                    position={positions.habits}
                    onStop={(e, data) => handleDragStop(e, data, "habits")}
                    disabled={!draggable}
                >
                    <div style={{ transform: `translate(${positions.habits.x}px, ${positions.habits.y}px)` }} className="absolute bg-white shadow-md flex items-center justify-center p-4 min-w-[300px] min-h-[200px]">
                        <HabitTracker />
                    </div>
                </Draggable>
            )}
            {views.includes("goals") && (
                <Draggable
                    position={positions.goals}
                    onStop={(e, data) => handleDragStop(e, data, "goals")}
                    disabled={!draggable}
                >
                    <div style={{ transform: `translate(${positions.goals.x}px, ${positions.goals.y}px)` }} className="absolute bg-white shadow-md flex items-center justify-center p-4 min-w-[300px] min-h-[200px]">
                        <GoalTable />
                    </div>
                </Draggable>
            )}
            {views.includes("statistics") && (
                <Draggable
                    position={positions.statistics}
                    onStop={(e, data) => handleDragStop(e, data, "statistics")}
                    disabled={!draggable}
                >
                    <div style={{ transform: `translate(${positions.statistics.x}px, ${positions.statistics.y}px)` }} className="absolute bg-white shadow-md flex items-center justify-center p-4 min-w-[300px] min-h-[200px]">
                        <Dashboard />
                    </div>
                </Draggable>
            )}
            {views.includes("chart") && (
                <Draggable
                    position={positions.chart}
                    onStop={(e, data) => handleDragStop(e, data, "chart")}
                    disabled={!draggable}
                >
                    <div style={{ transform: `translate(${positions.chart.x}px, ${positions.chart.y}px)` }} className="absolute bg-white shadow-md flex items-center justify-center p-4 min-w-[300px] min-h-[200px]">
                        <HabitChart />
                    </div>
                </Draggable>
            )}
        </div>
    );
}
