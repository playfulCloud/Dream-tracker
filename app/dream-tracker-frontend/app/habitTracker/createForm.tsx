import { useAppContext } from '../AppContext';
import { Button } from "@/components/ui/button";
import { FrequencyCombobox } from "@/app/habitTracker/frequencyCombobox";
import { DifficultyCombobox } from "@/app/habitTracker/difficultyCombobox";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import React from "react";
import axios from "axios";

export function CreateForm() {
    const { fetchHabits } = useAppContext();
    const [formData, setFormData] = React.useState({
        name: '',
        action: '',
        frequency: '',
        difficulty: ''
    });

    const [errors, setErrors] = React.useState({
        name: false,
        action: false,
        frequency: false,
        difficulty: false
    });

    const handleInputChange = (e) => {
        const { id, value } = e.target;
        setFormData((prevState) => ({
            ...prevState,
            [id]: value,
        }));
        setErrors((prevErrors) => ({
            ...prevErrors,
            [id]: false,
        }));
    };

    const handleFrequencyChange = (value) => {
        setFormData((prevState) => ({
            ...prevState,
            frequency: value,
        }));
        setErrors((prevErrors) => ({
            ...prevErrors,
            frequency: false,
        }));
    };

    const handleDifficultyChange = (value) => {
        setFormData((prevState) => ({
            ...prevState,
            difficulty: value,
        }));
        setErrors((prevErrors) => ({
            ...prevErrors,
            difficulty: false,
        }));
    };

    const getToken = (): string | null => {
        return localStorage.getItem('token');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Walidacja
        const newErrors = {
            name: formData.name === '',
            action: formData.action === '',
            frequency: formData.frequency === '',
            difficulty: formData.difficulty === ''
        };

        setErrors(newErrors);

        const hasErrors = Object.values(newErrors).some(error => error);
        if (hasErrors) {
            return;
        }

        try {
            const token = getToken();
            await axios.post('http://localhost:8080/v1/habits', formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            fetchHabits();
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <Dialog>
            <DialogTrigger asChild>
                <Button variant="outline">
                    Create habit to track
                </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle>Create habit to track</DialogTitle>
                    <DialogDescription>
                        Fill data about your habit.
                    </DialogDescription>
                </DialogHeader>
                <div className="grid gap-4 py-4">
                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="name" className="text-right">
                            Name
                        </Label>
                        <Input
                            id="name"
                            value={formData.name}
                            onChange={handleInputChange}
                            className={`col-span-3 ${errors.name ? 'border-red-500' : ''}`}
                        />
                        {errors.name && (
                            <p className="col-span-4 text-red-500 text-sm">Name is required.</p>
                        )}
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="action" className="text-right">
                            Action
                        </Label>
                        <Input
                            id="action"
                            value={formData.action}
                            onChange={handleInputChange}
                            className={`col-span-3 ${errors.action ? 'border-red-500' : ''}`}
                        />
                        {errors.action && (
                            <p className="col-span-4 text-red-500 text-sm">Action is required.</p>
                        )}
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="frequency" className="text-right">
                            Frequency
                        </Label>
                        <FrequencyCombobox onChange={handleFrequencyChange} />
                        {errors.frequency && (
                            <p className="col-span-4 text-red-500 text-sm">Frequency is required.</p>
                        )}
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="difficulty" className="text-right">
                            Difficulty
                        </Label>
                        <DifficultyCombobox onChange={handleDifficultyChange} />
                        {errors.difficulty && (
                            <p className="col-span-4 text-red-500 text-sm">Difficulty is required.</p>
                        )}
                    </div>
                </div>
                <DialogFooter>
                    <Button type="button" onClick={handleSubmit}>Save habit</Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
