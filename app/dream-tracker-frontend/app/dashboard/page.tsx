"use client";
import * as React from "react";
import axios from "axios";
import { useAppContext } from '../AppContext';
import StatsCard from "@/app/dashboard/StatsCard";
import { Card } from "@/components/ui/card";
import { HabitCombobox } from "@/app/goalTracker/HabitCombobox";
import { Calendar, TrendingUp, Clock, Award, BarChart } from "lucide-react"; // Import ikony

const getToken = (): string | null => {
    return localStorage.getItem('token');
};

export default function Dashboard() {
    const { habits } = useAppContext();
    const [selectedHabit, setSelectedHabit] = React.useState("");
    const [stats, setStats] = React.useState({
        averageBreak: 0,
        longest: 0,
        actual: 0,
        done: 0,
        undone: 0,
        trend: '',
        most: 0,
        actualSingleDay: 0,
        mondayRateSuccessRate: 0,
        tuesdayRateSuccessRate: 0,
        wednesdayRateSuccessRate: 0,
        thursdayRateSuccessRate: 0,
        fridayRateSuccessRate: 0,
        saturdayRateSuccessRate: 0,
        sundayRateSuccessRate: 0,
    });

    const fetchStats = async (habitId) => {
        try {
            const token = getToken(); // Pobierz token
            const response = await axios.get(`http://localhost:8080/v1/views/stats/${habitId}`, {
                headers: {
                    Authorization: `Bearer ${token}`, // Przekaż token w nagłówku
                },
            });
            setStats(response.data);
        } catch (error) {
            console.error("Error fetching stats:", error);
        }
    };

    React.useEffect(() => {
        if (selectedHabit) {
            fetchStats(selectedHabit);
        }
    }, [selectedHabit]);

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-4xl">
                <div className="mb-8">
                    <HabitCombobox
                        habits={habits}
                        value={selectedHabit}
                        onChange={setSelectedHabit}
                    />
                </div>
                <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-2 xl:grid-cols-2">
                    <StatsCard
                        cardTitle="Average Break"
                        cardDescription="Describes the average time between habit completion"
                        cardContent={`${stats.averageBreak} hours`}
                        icon={<Clock className="text-blue-500 w-10 h-10" />} // Ikona zegara
                    />
                    <StatsCard
                        cardTitle="Longest Streak"
                        cardDescription="Describes the number of periods of doing habit in a row"
                        cardContent={`${stats.longest} days`}
                        icon={<Award className="text-green-500 w-10 h-10" />} // Ikona trofeum
                    />
                    <StatsCard
                        cardTitle="Quantity of habits"
                        cardDescription="Counts number of done and undone habits and calculates trend based on them"
                        cardContent={`Done: ${stats.done}, Undone: ${stats.undone}, Trend: ${stats.trend}`}
                        icon={<BarChart className="text-yellow-500 w-10 h-10" />} // Ikona wykresu
                    />
                    <StatsCard
                        cardTitle="Best Day for Habit"
                        cardDescription="Describes the best day for habit completion"
                        cardContent={`Most Completions: ${stats.most}, Actual on Single Day: ${stats.actualSingleDay}`}
                        icon={<Calendar className="text-red-500 w-10 h-10" />} // Ikona kalendarza
                    />
                    <StatsCard
                        cardTitle="Success Rate by Day"
                        cardDescription="Shows the success rate by day of the week"
                        cardContent={`
                            Mon: ${stats.mondayRateSuccessRate}%, 
                            Tue: ${stats.tuesdayRateSuccessRate}%, 
                            Wed: ${stats.wednesdayRateSuccessRate}%, 
                            Thu: ${stats.thursdayRateSuccessRate}%, 
                            Fri: ${stats.fridayRateSuccessRate}%, 
                            Sat: ${stats.saturdayRateSuccessRate}%, 
                            Sun: ${stats.sundayRateSuccessRate}%
                        `}
                        icon={<TrendingUp className="text-purple-500 w-10 h-10" />} // Ikona trendu
                    />
                </div>
            </div>
        </div>
    );
}
