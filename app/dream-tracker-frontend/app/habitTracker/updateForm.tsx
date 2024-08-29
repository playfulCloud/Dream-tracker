import { useAppContext } from '../AppContext';
import { Button } from "@/components/ui/button"
import { FrequencyCombobox } from "@/app/habitTracker/frequencyCombobox";
import { DifficultyCombobox } from "@/app/habitTracker/difficultyCombobox";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import React from "react";
import axios from "axios";

export function UpdateForm({ habit, isOpen, onClose }) {
    const { fetchHabits } = useAppContext();
    const [formData, setFormData] = React.useState({
        name: habit?.name || '',
        action: habit?.action || '',
        frequency: habit?.frequency || '',
        difficulty: habit?.difficulty || ''
    });

    const handleInputChange = (e) => {
        const { id, value } = e.target;
        setFormData((prevState) => ({
            ...prevState,
            [id]: value,
        }));
    };

    const handleFrequencyChange = (value) => {
        setFormData((prevState) => ({
            ...prevState,
            frequency: value,
        }));
    };

    const handleDifficultyChange = (value) => {
        setFormData((prevState) => ({
            ...prevState,
            difficulty: value,
        }));
    };

    const getToken = (): string | null => {
        return localStorage.getItem('token');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log(formData);
        try {
            const token = getToken();
            await axios.put(`http://localhost:8080/v1/habits/${habit.id}`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            fetchHabits();
            onClose(); // Zamknij dialog po udanej aktualizacji
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="sm:max-w-[425px]">
                <form onSubmit={handleSubmit}>
                    <DialogHeader>
                        <DialogTitle>Update habit</DialogTitle>
                        <DialogDescription>
                            Update the details of your habit.
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
                                className="col-span-3"
                            />
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="action" className="text-right">
                                Action
                            </Label>
                            <Input
                                id="action"
                                value={formData.action}
                                onChange={handleInputChange}
                                className="col-span-3"
                            />
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="frequency" className="text-right">
                                Frequency
                            </Label>
                            <FrequencyCombobox
                                value={formData.frequency}
                                onChange={handleFrequencyChange}
                            />
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="difficulty" className="text-right">
                                Difficulty
                            </Label>
                            <DifficultyCombobox
                                value={formData.difficulty}
                                onChange={handleDifficultyChange}
                            />
                        </div>
                    </div>
                    <DialogFooter>
                        <Button type="submit">Save habit</Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}
