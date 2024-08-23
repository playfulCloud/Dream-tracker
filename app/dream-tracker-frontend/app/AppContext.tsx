"use client";
import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import axios from 'axios';

interface Habit {
    id: string;
    name: string;
    action: string;
    duration: string;
    difficulty: string;
    status: string;
    categories: Category[];
}

interface GoalResponse {
    uuid: string;
    name: string;
    duration: string;
    status: string;
    habitUUID: string;
    completionCount: number;
    currentCount: number;
}

interface ChartData {
    date: string;
    count: number;
    all: number;
}

interface AppContextProps {
    habits: Habit[];
    goals: GoalResponse[];
    categories: Category[];
    loading: boolean;
    error: string | null;
    views: string[];
    toggleView: (view: string) => void;
    fetchGoals: () => void;
    fetchHabits: () => void;
    fetchCategories: () => void;
    fetchStats: (habitId: string) => Promise<void>;
    fetchCharts: () => Promise<void>;  // Add the fetchCharts function
    stats: Record<string, any>;
    chartData: ChartData[];  // Add chartData to the context
}

interface Category {
    id: string;
    name: string;
}

const AppContext = createContext<AppContextProps | undefined>(undefined);

export const AppProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [habits, setHabits] = useState<Habit[]>([]);
    const [goals, setGoals] = useState<GoalResponse[]>([]);
    const [categories, setCategories] = useState<Category[]>([]);
    const [stats, setStats] = useState<Record<string, any>>({});
    const [chartData, setChartData] = useState<ChartData[]>([]);  // State for chart data
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [views, setViews] = useState<string[]>(["habits"]);

    const getToken = () => localStorage.getItem('token');

    const toggleView = (view: string) => {
        setViews((prevViews) => {
            const newViews = prevViews.includes(view)
                ? prevViews.filter((v) => v !== view)
                : [...prevViews, view];
            return newViews;
        });
    };

    const fetchGoals = async () => {
        setLoading(true);
        try {
            const token = getToken();
            const response = await axios.get<{ items: GoalResponse[] }>('http://localhost:8080/v1/goals', {
                headers: { Authorization: `Bearer ${token}` },
            });
            const goalData = response.data.items || [];
            setGoals(goalData);
            localStorage.setItem('goals', JSON.stringify(goalData));
        } catch (error) {
            console.error(error);
            setError('Failed to fetch goals');
        } finally {
            setLoading(false);
        }
    };

    const fetchCategories = async () => {
        setLoading(true);
        try {
            const token = getToken();
            const response = await axios.get<{ items: Category[] }>('http://localhost:8080/v1/categories', {
                headers: { Authorization: `Bearer ${token}` },
            });
            const categoryData = response.data.items || [];
            setCategories(categoryData);
            localStorage.setItem('categories', JSON.stringify(categoryData));
        } catch (error) {
            console.error(error);
            setError('Failed to fetch categories');
        } finally {
            setLoading(false);
        }
    };

    const fetchHabits = async () => {
        setLoading(true);
        try {
            const token = getToken();
            const response = await axios.get<{ items: Habit[] }>('http://localhost:8080/v1/habits', {
                headers: { Authorization: `Bearer ${token}` },
            });
            const habitData = response.data.items || [];
            setHabits(habitData);
            localStorage.setItem('habits', JSON.stringify(habitData));
        } catch (error) {
            console.error(error);
            setError('Failed to fetch habits');
        } finally {
            setLoading(false);
        }
    };

    const fetchStats = useCallback(async (habitId: string) => {
        try {
            const token = getToken();
            const response = await axios.get(`http://localhost:8080/v1/views/stats/${habitId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setStats((prevStats) => ({
                ...prevStats,
                [habitId]: response.data,
            }));
        } catch (error) {
            console.error("Error fetching stats:", error);
        }
    }, []);

    const fetchCharts = useCallback(async () => {
        try {
            const token = getToken();
            const response = await axios.get<{ items: ChartData[] }>('http://localhost:8080/v1/habitCharts', {
                headers: { Authorization: `Bearer ${token}` },
            });
            const chartData = response.data.items || [];
            setChartData(chartData);
        } catch (error) {
            console.error("Error fetching chart data:", error);
        }
    }, []);

    useEffect(() => {
        fetchGoals();
        fetchHabits();
        fetchCategories();
        fetchCharts();

        const handleStorageChange = (event: StorageEvent) => {
            if (event.key === 'goals') {
                setGoals(JSON.parse(event.newValue || '[]'));
            }
            if (event.key === 'habits') {
                setHabits(JSON.parse(event.newValue || '[]'));
            }
            if (event.key === 'categories') {
                setCategories(JSON.parse(event.newValue || '[]'));
            }
        };

        window.addEventListener('storage', handleStorageChange);
        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, [fetchCharts]);

    return (
        <AppContext.Provider value={{ habits, goals, categories, loading, error, views, toggleView, fetchGoals, fetchHabits, fetchCategories, fetchStats, fetchCharts, stats, chartData }}>
            {children}
        </AppContext.Provider>
    );
};

export const useAppContext = () => {
    const context = useContext(AppContext);
    if (context === undefined) {
        throw new Error('useAppContext must be used within an AppProvider');
    }
    return context;
};
