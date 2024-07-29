"use client";
import React, { useState, useEffect } from 'react';
import axios from 'axios';

interface Habit {
    id: string;
    name: string;
    action: string;
    duration: string;
    difficulty: string;
    status: string;
}

export interface GoalResponse {
    uuid: string;
    name: string;
    duration: string;
    habitUUID: string;
    completionCount: number;
    currentCount: number;
}

interface GoalRequest {
    name: string;
    duration: string;
    completionCount: number;
    habitID: string;
}

const Goals = () => {
    const [goals, setGoals] = useState<GoalResponse[]>([]);
    const [habits, setHabits] = useState<Habit[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [formVisible, setFormVisible] = useState(false);
    const [formData, setFormData] = useState<GoalRequest>({ name: '', duration: '', completionCount: 0, habitID: '' });

    const fetchGoals = async () => {
        try {
            const response = await axios.get<{ items: GoalResponse[] }>('http://localhost:8080/v1/goals');
            const goalData = response.data && response.data.items ? response.data.items : []; // Ensure goals is always an array
            setGoals(goalData);
            setLoading(false);
        } catch (error) {
            console.error(error);
            setError('Failed to fetch goals');
            setLoading(false);
        }
    };

    const fetchHabits = async () => {
        try {
            const response = await axios.get<{ items: Habit[] }>('http://localhost:8080/v1/habits');
            const habitData = response.data && response.data.items ? response.data.items : []; // Ensure habits is always an array
            setHabits(habitData);
        } catch (error) {
            console.error(error);
            setError('Failed to fetch habits');
        }
    };

    useEffect(() => {
        fetchGoals();
        fetchHabits(); // Fetch habits when component mounts
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        console.log(formData)
        try {
            const response = await axios.post<GoalResponse>('http://localhost:8080/v1/goals', formData);
            console.log(response.data);
            setFormVisible(false); // Hide form after submission
            fetchGoals();
        } catch (error) {
            console.error(error);
        }
    };

    const calculateProgress = (goal: GoalResponse) => {
        return (goal.currentCount / goal.completionCount) * 100;
    };

    return (
        <div>
            <h2 className="text-lg font-medium text-gray-700">Goals List</h2>
            <button className="text-black" onClick={() => setFormVisible(!formVisible)}>
                {formVisible ? 'Hide Form' : 'Add New Goal'}
            </button>
            {formVisible && (
                <form onSubmit={handleSubmit} className="space-y-4 mt-4">
                    <div>
                        <label htmlFor="name" className="block text-sm font-medium text-gray-700">Name</label>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="duration" className="block text-sm font-medium text-gray-700">Duration</label>
                        <input
                            type="text"
                            name="duration"
                            value={formData.duration}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="completionCount" className="block text-sm font-medium text-gray-700">Completion Count</label>
                        <input
                            type="number"
                            name="completionCount"
                            value={formData.completionCount}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="habitID" className="block text-sm font-medium text-gray-700">Select Habit</label>
                        <select
                            name="habitID"
                            value={formData.habitID}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                            required
                        >
                            <option value="" disabled>Select a habit</option>
                            {habits.map(habit => (
                                <option key={habit.id} value={habit.id}>{habit.name}</option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <button
                            type="submit"
                            className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700 transition duration-300"
                        >
                            Submit
                        </button>
                    </div>
                </form>
            )}
            {loading && <p>Loading...</p>}
            {error && <p className="text-red-500">{error}</p>}
            <ul className="space-y-2 mt-4">
                {goals.length > 0 ? (
                    goals.map(goal => (
                        <li key={goal.uuid} className="p-4 border border-gray-300 rounded text-black">
                            <p><strong>Name:</strong> {goal.name}</p>
                            <p><strong>Duration:</strong> {goal.duration}</p>
                            <p><strong>Completion Count:</strong> {goal.completionCount}</p>
                            <p><strong>Current Count:</strong> {goal.currentCount}</p>
                            <div className="relative pt-1">
                                <div className="flex mb-2 items-center justify-between">
                                    <div>
                                        <span className="text-xs font-semibold inline-block py-1 px-2 uppercase rounded-full text-blue-600 bg-blue-200">
                                            Progress
                                        </span>
                                    </div>
                                    <div className="text-right">
                                        <span className="text-xs font-semibold inline-block text-blue-600">
                                            {Math.round(calculateProgress(goal))}%
                                        </span>
                                    </div>
                                </div>
                                <div className="overflow-hidden h-2 mb-4 text-xs flex rounded bg-blue-200">
                                    <div style={{ width: `${calculateProgress(goal)}%` }} className="shadow-none flex flex-col text-center whitespace-nowrap text-white justify-center bg-blue-500"></div>
                                </div>
                            </div>
                        </li>
                    ))
                ) : (
                    !loading && <p>No goals found.</p>
                )}
            </ul>
        </div>
    );
}

export default Goals;
