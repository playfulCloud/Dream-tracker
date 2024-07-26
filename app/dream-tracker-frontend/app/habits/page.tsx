"use client";
import React, { useState } from 'react';
import axios from 'axios';


interface HabitResponse {
    uuid: string;
    name: string;
    action: string;
    duration: string;
    difficulty: string;
    status: string;
}

const habits = () => {
    const [formVisible, setFormVisible] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        action: '',
        frequency: '',
        duration: '',
        difficulty: ''
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post<HabitResponse>('http://localhost:8080/v1/habits', formData);
            console.log(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div>
            <button onClick={() => setFormVisible(!formVisible)}>
                {formVisible ? 'Hide Form' : 'Show Form'}
            </button>
            {formVisible && (
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="name" className="block text-sm font-medium text-gray-700">Name</label>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                        />
                    </div>
                    <div>
                        <label htmlFor="action" className="block text-sm font-medium text-gray-700">Action</label>
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
        </div>
    );
}

export default habits;