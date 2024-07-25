"use client";
import React, { useState } from 'react';
import axios from 'axios';


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
            const response = await axios.post('/habits', formData);
            console.log(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div className="p-6">
            <button
                className="bg-blue-500 text-white font-bold py-2 px-4 rounded hover:bg-blue-700 transition duration-300"
                onClick={() => setFormVisible(!formVisible)}
            >
                {formVisible ? 'Hide Form' : 'Add Habit'}
            </button>
            {formVisible && (
                <form onSubmit={handleSubmit} className="mt-6 space-y-4">
                    <div className="flex flex-col">
                        <label className="mb-2 font-semibold">Name:</label>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            className="p-2 border border-gray-300 rounded text-black"
                            required
                        />
                    </div>
                    <div className="flex flex-col">
                        <label className="mb-2 font-semibold">Action:</label>
                        <input
                            type="text"
                            name="action"
                            value={formData.action}
                            onChange={handleChange}
                            className="p-2 border border-gray-300 rounded text-black"
                            required
                        />
                    </div>
                    <div className="flex flex-col">
                        <label className="mb-2 font-semibold">Frequency:</label>
                        <input
                            type="text"
                            name="frequency"
                            value={formData.frequency}
                            onChange={handleChange}
                            className="p-2 border border-gray-300 rounded text-black"
                            required
                        />
                    </div>
                    <div className="flex flex-col">
                        <label className="mb-2 font-semibold">Duration:</label>
                        <input
                            type="text"
                            name="duration"
                            value={formData.duration}
                            onChange={handleChange}
                            className="p-2 border border-gray-300 rounded text-black"
                            required
                        />
                    </div>
                    <div className="flex flex-col">
                        <label className="mb-2 font-semibold">Difficulty:</label>
                        <input
                            type="text"
                            name="difficulty"
                            value={formData.difficulty}
                            onChange={handleChange}
                            className="p-2 border border-gray-300 rounded text-black"
                            required
                        />
                    </div>
                    <button
                        type="submit"
                        className="bg-green-500 text-white font-bold py-2 px-4 rounded hover:bg-green-700 transition duration-300"
                    >
                        Submit
                    </button>
                </form>
            )}
        </div>
    );
}

export default habits;