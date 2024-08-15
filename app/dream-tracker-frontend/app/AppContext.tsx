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
    categories: Category[]; // Ensure categories is part of Habit interface
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
    categories: Category[]; // Ensure categories is part of context
    loading: boolean;
    error: string | null;
    fetchGoals: () => void;
    fetchHabits: () => void;
    fetchCategories: () => void;
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
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const getToken = () => {
        return localStorage.getItem('token');
    };

    const fetchGoals = async () => {
        setLoading(true);
        try {
            const token = getToken();
            const response = await axios.get<{ items: GoalResponse[] }>('http://localhost:8080/v1/goals', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
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
                headers: {
                    Authorization: `Bearer ${token}`,
                },
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
                headers: {
                    Authorization: `Bearer ${token}`,
                },
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

    useEffect(() => {
        fetchGoals();
        fetchHabits();
        fetchCategories();
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
    }, []);

    return (
        <AppContext.Provider value={{ habits, goals, categories, loading, error, fetchGoals, fetchHabits, fetchCategories }}>
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
