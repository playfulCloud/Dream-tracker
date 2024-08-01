// app/AppContext.tsx
"use client";

import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

interface Habit {
    id: string;
    name: string;
    action: string;
    duration: string;
    difficulty: string;
    status: string;
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

interface AppContextProps {
    habits: Habit[];
    goals: GoalResponse[];
    loading: boolean;
    error: string | null;
    fetchGoals: () => void;
    fetchHabits: () => void;
}

const AppContext = createContext<AppContextProps | undefined>(undefined);

export const AppProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [habits, setHabits] = useState<Habit[]>([]);
    const [goals, setGoals] = useState<GoalResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchGoals = async () => {
        setLoading(true);
        try {
            const response = await axios.get<{ items: GoalResponse[] }>('http://localhost:8080/v1/goals');
            const goalData = response.data && response.data.items ? response.data.items : [];
            setGoals(goalData);
            localStorage.setItem('goals', JSON.stringify(goalData)); // Write to localStorage
            console.log(goalData)
        } catch (error) {
            console.error(error);
            setError('Failed to fetch goals');
        } finally {
            setLoading(false);
        }
    };

    const fetchHabits = async () => {
        setLoading(true);
        try {
            const response = await axios.get<{ items: Habit[] }>('http://localhost:8080/v1/habits');
            const habitData = response.data && response.data.items ? response.data.items : [];
            setHabits(habitData);
            localStorage.setItem('habits', JSON.stringify(habitData)); // Write to localStorage
        } catch (error) {
            console.error(error);
            setError('Failed to fetch habits');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchGoals();
        fetchHabits();

        // Listen for localStorage changes
        const handleStorageChange = (event: StorageEvent) => {
            if (event.key === 'goals') {
                setGoals(JSON.parse(event.newValue || '[]'));
            }
            if (event.key === 'habits') {
                setHabits(JSON.parse(event.newValue || '[]'));
            }
        };

        window.addEventListener('storage', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);

    return (
        <AppContext.Provider value={{ habits, goals, loading, error, fetchGoals, fetchHabits }}>
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
