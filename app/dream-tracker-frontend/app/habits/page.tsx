// app/habits/page.tsx
"use client";

import React, { useState } from 'react';
import { useAppContext } from '../AppContext';
import axios from 'axios';

interface HabitResponse {
    id: string;
    name: string;
    action: string;
    duration: string;
    difficulty: string;
    status: string;
}

interface HabitTrackResponse {
    date: string;
    status: string;
}

const Habits = () => {
    const { habits, loading, error, fetchHabits, fetchGoals } = useAppContext();
    const [formVisible, setFormVisible] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        action: '',
        frequency: '',
        duration: '',
        difficulty: ''
    });
    const [checkedHabits, setCheckedHabits] = useState<string[]>([]);
    const [successHabit, setSuccessHabit] = useState<string | null>(null);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response = await axios.post<HabitResponse>('http://localhost:8080/v1/habits', formData);
            fetchHabits(); // Fetch updated habits list after submitting form
        } catch (error) {
            console.error(error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await axios.delete(`http://localhost:8080/v1/habits/${id}`);
            fetchHabits(); // Fetch updated habits list after deleting a habit
        } catch (error) {
            console.error('Failed to delete habit', error);
        }
    };

    const handleTracking = async (id: string) => {
        try {
            const habitTrackingRequest = { habitId: id, status: "DONE" };
            const response = await axios.post<HabitTrackResponse>('http://localhost:8080/v1/habits-tracking', habitTrackingRequest);
            setCheckedHabits([...checkedHabits, id]);
            setSuccessHabit(id);
            setTimeout(() => setSuccessHabit(null), 2000);
            fetchHabits(); // Fetch updated habits list after tracking a habit
            fetchGoals(); // Fetch updated goals list after tracking a habit
        } catch (error) {
            console.error('Failed to track habit', error);
        }
    };

    return (
        <div>
            <button className="text-black" onClick={() => setFormVisible(!formVisible)}>
                {formVisible ? 'x' : '+'}
            </button>
            {formVisible && (
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="name" className="block text-sm font-medium text-black-700">Name</label>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                        />
                    </div>
                    <div>
                        <label htmlFor="action" className="block text-sm font-medium text-black-700">Action</label>
                        <input
                            type="text"
                            name="action"
                            value={formData.action}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                        />
                    </div>
                    <div>
                        <label htmlFor="frequency" className="block text-sm font-medium text-gray-700">Frequency</label>
                        <select
                            name="frequency"
                            value={formData.frequency}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                        >
                            <option value="">Select Frequency</option>
                            <option value="DAILY">Daily</option>
                            <option value="WEEKLY">Weekly</option>
                            <option value="MONTHLY">Monthly</option>
                        </select>
                    </div>
                    <div>
                        <label htmlFor="duration" className="block text-sm font-medium text-gray-700">Duration</label>
                        <input
                            type="text"
                            name="duration"
                            value={formData.duration}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                        />
                    </div>
                    <div>
                        <label htmlFor="difficulty" className="block text-sm font-medium text-gray-700">Difficulty</label>
                        <select
                            name="difficulty"
                            value={formData.difficulty}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                        >
                            <option value="">Select Difficulty</option>
                            <option value="EASY">Easy</option>
                            <option value="MEDIUM">Medium</option>
                            <option value="HARD">Hard</option>
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
            <div className="mt-4">
                <h2 className="text-lg font-medium text-gray-700">Habits List</h2>
                {loading && <p>Loading...</p>}
                {error && <p className="text-red-500">{error}</p>}
                <ul className="space-y-2">
                    {habits.length > 0 ? (
                        habits.map(habit => (
                            <li
                                key={habit.id}
                                className={`p-4 border border-gray-300 rounded text-black relative transition duration-300 ${
                                    checkedHabits.includes(habit.id) ? 'bg-gray-300' : ''
                                } ${successHabit === habit.id ? 'bg-green-300' : ''}`}
                            >
                                <button
                                    className="absolute top-2 right-2 text-red-500"
                                    onClick={() => handleDelete(habit.id)}
                                >
                                    X
                                </button>
                                <input
                                    type="checkbox"
                                    className="absolute transform -translate-y-1/2 right-4"
                                    style={{ top: '50%', width: '40px', height: '40px' }}
                                    onChange={() => handleTracking(habit.id)}
                                    checked={checkedHabits.includes(habit.id)}
                                />
                                <p><strong>Name:</strong> {habit.name}</p>
                                <p><strong>Action:</strong> {habit.action}</p>
                                <p><strong>Duration:</strong> {habit.duration}</p>
                                <p><strong>Difficulty:</strong> {habit.difficulty}</p>
                                <p><strong>Status:</strong> {habit.status}</p>
                            </li>
                        ))
                    ) : (
                        !loading && <p>No habits found.</p>
                    )}
                </ul>
            </div>
        </div>
    );
}

export default Habits;
