"use client";

import React, { useState, useEffect } from 'react';
import { useAppContext } from '../AppContext';
import axios from "axios";

interface CategoryRequest {
    name: string;
}

interface CategoryResponse {
    id: string;
    name: string;
}

interface HabitCategoryCreateRequest {
    id: string;
}

const Categories = () => {
    const { categories, habits, loading, error, fetchCategories, fetchHabits } = useAppContext();
    const [formVisible, setFormVisible] = useState(false);
    const [formData, setFormData] = useState<CategoryRequest>({ name: '' });
    const [habitFormData, setHabitFormData] = useState<HabitCategoryCreateRequest>({ id: '' });
    const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
    const [selectedHabit, setSelectedHabit] = useState<string | null>(null);

    useEffect(() => {
        if (fetchCategories) {
            fetchCategories();
        }
        if (fetchHabits) {
            fetchHabits();
        }
    }, []); // Empty dependency array ensures this useEffect runs only once after initial render.

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleHabitChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setHabitFormData({
            ...habitFormData,
            [e.target.name]: e.target.value
        });
    };

    const handleHabitSelect = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedHabit(e.target.value);
    };

    const handleCategorySelect = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setHabitFormData({
            ...habitFormData,
            id: e.target.value
        });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            await axios.post<CategoryResponse>('http://localhost:8080/v1/categories', formData);
            setFormVisible(false);
            if (fetchCategories) {
                fetchCategories();
            }
        } catch (error) {
            console.error(error);
        }
    };

    const handleAddHabit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!selectedHabit || !habitFormData.id) {
            console.error('Habit ID and Category ID are required');
            return;
        }
        try {
            await axios.post(`http://localhost:8080/v1/habits/${selectedHabit}/categories`, habitFormData);
            setSelectedCategory(null);
            setSelectedHabit(null);
            if (fetchHabits) {
                fetchHabits();
            }
        } catch (error) {
            console.error(error);
        }
    };

    const handleDelete = async (id: string) => {
        console.log('Attempting to delete category with id:', id);
        try {
            const response = await axios.delete(`http://localhost:8080/v1/categories/${id}`);
            console.log(response);
            if (response.status === 204) {
                console.log('Category deleted successfully');
                if (fetchCategories) {
                    fetchCategories();
                    fetchHabits();
                }
            } else {
                console.error(`Unexpected response status: ${response.status}`);
            }
            fetchHabits();
        } catch (error) {
            console.error('Error deleting category:', error);
        }
    };

    const handleUpdate = async (id: string) => {
        try {
            await axios.put(`http://localhost:8080/v1/categories/${id}`, formData);
            setSelectedCategory(null);
            if (fetchCategories) {
                fetchCategories();
            }
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div>
            <h2 className="text-lg font-medium text-gray-700">Categories List</h2>
            <button className="text-black" onClick={() => setFormVisible(!formVisible)}>
                {formVisible ? 'Hide Form' : 'Add New Category'}
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
                {categories && categories.length > 0 ? (
                    categories.map(category => (
                        <li key={category.id} className="p-4 border border-gray-300 rounded text-black bg-gray-100">
                            <p><strong>Name:</strong> {category.name}</p>
                            <button
                                className="text-red-500 underline"
                                onClick={() => handleDelete(category.id)}
                            >
                                Delete
                            </button>
                            <button
                                className="text-blue-500 underline ml-4"
                                onClick={() => setSelectedCategory(category.id)}
                            >
                                Add Habit
                            </button>
                            {selectedCategory === category.id && (
                                <form onSubmit={handleAddHabit} className="space-y-4 mt-4">
                                    <div>
                                        <label htmlFor="habitId" className="block text-sm font-medium text-gray-700">Select Habit</label>
                                        <select
                                            name="habitId"
                                            value={selectedHabit || ''}
                                            onChange={handleHabitSelect}
                                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                                            required
                                        >
                                            <option value="" disabled>Select Habit</option>
                                            {habits.map(habit => (
                                                <option key={habit.id} value={habit.id}>
                                                    {habit.name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="categoryId" className="block text-sm font-medium text-gray-700">Assign to Category</label>
                                        <select
                                            name="categoryId"
                                            value={habitFormData.id}
                                            onChange={handleCategorySelect}
                                            className="mt-1 block w-full p-2 border border-gray-300 rounded text-black"
                                            required
                                        >
                                            <option value="" disabled>Select Category</option>
                                            {categories.map(categoryOption => (
                                                <option key={categoryOption.id} value={categoryOption.id}>
                                                    {categoryOption.name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    <div>
                                        <button
                                            type="submit"
                                            className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700 transition duration-300"
                                        >
                                            Add Habit
                                        </button>
                                    </div>
                                </form>
                            )}
                        </li>
                    ))
                ) : (
                    !loading && <p>No categories found.</p>
                )}
            </ul>
            <h2 className="text-lg font-medium text-gray-700 mt-8">Active Habits List</h2>
            <ul className="space-y-2 mt-4">
                {habits && habits.length > 0 ? (
                    habits
                        .filter(habit => habit.status === 'ACTIVE')
                        .map(habit => (
                            <li key={habit.id} className="p-4 border border-gray-300 rounded text-black bg-gray-100">
                                <p><strong>Name:</strong> {habit.name}</p>
                                <p><strong>Categories:</strong> {habit.categories && habit.categories.length > 0 ? habit.categories.map(category => category.name).join(', ') : 'No categories assigned'}</p>
                            </li>
                        ))
                ) : (
                    !loading && <p>No active habits found.</p>
                )}
            </ul>
        </div>
    );
}

export default Categories;
