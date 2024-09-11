import React from "react";
import { useAppContext } from '../AppContext';
import { Button } from "@/components/ui/button";
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
import axios from "axios";
import { HabitCombobox } from "./HabitCombobox";
import { CalendarForGoals } from "@/app/goalTracker/isoCalendar";

export function GoalCreateForm() {
    const { habits, fetchGoals } = useAppContext();
    const [formData, setFormData] = React.useState({
        name: '',
        duration: '',
        completionCount: 0,
        habitID: ''
    });

    const [errors, setErrors] = React.useState({
        name: false,
        duration: false,
        completionCount: false,
        habitID: false
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

    const handleHabitChange = (habitID) => {
        setFormData((prevState) => ({
            ...prevState,
            habitID,
        }));
        setErrors((prevErrors) => ({
            ...prevErrors,
            habitID: false,
        }));
    };

    const handleDateChange = (isoPeriod) => {
        setFormData((prevState) => ({
            ...prevState,
            duration: isoPeriod,
        }));
        setErrors((prevErrors) => ({
            ...prevErrors,
            duration: false,
        }));
    };

    const getToken = () => {
        return localStorage.getItem('token');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const newErrors = {
            name: formData.name === '',
            duration: formData.duration === '',
            completionCount: formData.completionCount <= 0,
            habitID: formData.habitID === ''
        };

        setErrors(newErrors);

        const hasErrors = Object.values(newErrors).some(error => error);
        if (hasErrors) {
            return;
        }

        try {
            const token = getToken();
            await axios.post('http://localhost:8080/v1/goals', formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            fetchGoals();
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <Dialog>
            <DialogTrigger asChild>
                <Button variant="outline">
                    Create New Goal to do
                </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle>Create New Goal</DialogTitle>
                    <DialogDescription>
                        Fill in the details for your new goal.
                    </DialogDescription>
                </DialogHeader>
                <form onSubmit={handleSubmit}>
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
                            <Label htmlFor="duration" className="text-right">
                                Duration
                            </Label>
                            <div className="col-span-3">
                                <CalendarForGoals onDateSelect={handleDateChange} />
                            </div>
                            {errors.duration && (
                                <p className="col-span-4 text-red-500 text-sm">Duration is required.</p>
                            )}
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="completionCount" className="text-right">
                                Completion Count
                            </Label>
                            <Input
                                id="completionCount"
                                type="number"
                                value={formData.completionCount}
                                onChange={handleInputChange}
                                className={`col-span-3 ${errors.completionCount ? 'border-red-500' : ''}`}
                            />
                            {errors.completionCount && (
                                <p className="col-span-4 text-red-500 text-sm">Completion Count must be greater than 0.</p>
                            )}
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="habitID" className="text-right">
                                Select Habit
                            </Label>
                            <HabitCombobox
                                habits={habits}
                                value={formData.habitID}
                                onChange={handleHabitChange}
                            />
                            {errors.habitID && (
                                <p className="col-span-4 text-red-500 text-sm">Selecting a habit is required.</p>
                            )}
                        </div>
                    </div>
                    <DialogFooter>
                        <Button type="submit">Save Goal</Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}
