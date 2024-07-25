"use client";
import React from 'react';
import axios from 'axios';

interface UserResponse {
    uuid: string;
    name: string;
    surname: string;
}

const Users = () => {
    const createSampleUser = async () => {
        try {
            const response = await axios.post<UserResponse>('http://localhost:8080/v1/seed');
            console.log('User created:', response.data);
        } catch (error) {
            console.error('Error creating user:', error);
        }
    };

    return (
        <div>
                <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={createSampleUser}>Create Sample User</button>
        </div>
);
};

export default Users;
