"use client"
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
    id: string;
    name: string;
    duration: string;
    habitList: Habit[];
}

interface GoalRequest {
    name: string;
    duration: string;
}


const Goals = () => {
    const [goals, setGoals] = useState<GoalResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [formVisible, setFormVisible] = useState(false);
    const [formData, setFormData] = useState<GoalRequest>({ name: '', duration: '' });

    const fetchGoals = async () => {
        try {
            const response = await axios.get<GoalResponse>('http://localhost:8080/v1/goals');
            const goalData = response.data && response.data.items ? response.data.items : []; // Ensure goals is always an array
            setGoals(goalData);
            setLoading(false);
        } catch (error) {
            console.error(error);
            setError('Failed to fetch goals');
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchGoals();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response = await axios.post<GoalResponse>('http://localhost:8080/v1/goals', formData);
            console.log(response.data);
            setFormVisible(false); // Hide form after submission
            fetchGoals(); // Fetch updated goals list after submitting form
        } catch (error) {
            console.error(error);
        }
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
                        <li key={goal.id} className="p-4 border border-gray-300 rounded text-black">
                            <p><strong>Name:</strong> {goal.name}</p>
                            <p><strong>Duration:</strong> {goal.duration}</p>
                            <p><strong>Habits:</strong></p>
                            <ul className="ml-4 list-disc">
                                {goal.habitList ? (
                                    goal.habitList.map(habit => (
                                        <li key={habit.id}>
                                            <p><strong>Habit Name:</strong> {habit.name}</p>
                                            <p><strong>Action:</strong> {habit.action}</p>
                                            <p><strong>Duration:</strong> {habit.duration}</p>
                                            <p><strong>Difficulty:</strong> {habit.difficulty}</p>
                                            <p><strong>Status:</strong> {habit.status}</p>
                                        </li>
                                    ))
                                ) : (
                                    <p>No habits found.</p>
                                )}
                            </ul>
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
