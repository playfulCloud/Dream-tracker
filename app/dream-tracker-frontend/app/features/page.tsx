import {  FlagIcon, ChartBarIcon } from '@heroicons/react/20/solid'
import {ClipboardCheckIcon} from "lucide-react";


const features = [
    {
        name: 'Track Your Habits',
        description:
            'Monitor your daily habits with ease. Stay on top of your routines and make sure you’re hitting your goals every day.',
        icon: ClipboardCheckIcon,
    },
    {
        name: 'Set Achievable Goals',
        description: 'Create and manage your goals with precision. Break down big ambitions into smaller, actionable steps.',
        icon: FlagIcon,
    },
    {
        name: 'Analyze Your Progress',
        description: 'View detailed statistics and insights to understand your progress and improve your habits over time.',
        icon: ChartBarIcon,
    },
]

export default function Features() {
    return (
        <div className="relative isolate overflow-hidden bg-gray-900 min-h-screen flex items-center justify-center">
            <div className="absolute inset-x-0 -top-40 -z-10 transform-gpu overflow-hidden blur-3xl sm:-top-80">
                <div
                    style={{
                        clipPath:
                            'polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)',
                    }}
                    className="relative left-[calc(50%-11rem)] aspect-[1155/678] w-[36.125rem] -translate-x-1/2 rotate-[30deg] bg-gradient-to-tr from-[#1f1f3a] to-[#2d2d6c] opacity-30 sm:left-[calc(50%-30rem)] sm:w-[72.1875rem]"
                />
            </div>
            <div className="absolute inset-x-0 top-[calc(100%-13rem)] -z-10 transform-gpu overflow-hidden blur-3xl sm:top-[calc(100%-30rem)]">
                <div
                    style={{
                        clipPath:
                            'polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)',
                    }}
                    className="relative left-[calc(50%+3rem)] aspect-[1155/678] w-[36.125rem] -translate-x-1/2 bg-gradient-to-tr from-[#1f1f3a] to-[#2d2d6c] opacity-30 sm:left-[calc(50%+36rem)] sm:w-[72.1875rem]"
                />
            </div>
            <div className="mx-auto max-w-2xl px-6 lg:px-8 text-center">
                <div className="lg:pr-8 lg:pt-4">
                    <div className="lg:max-w-lg">
                        <h2 className="text-base font-semibold leading-7 text-indigo-400">Boost Your Productivity</h2>
                        <p className="mt-2 text-3xl font-bold tracking-tight text-white sm:text-4xl">Stay on Track</p>
                        <p className="mt-6 text-lg leading-8 text-gray-300">
                            Our habit tracker helps you build and maintain good habits. Whether it's a daily routine or a long-term goal, our tools will help you stay focused and motivated.
                        </p>
                        <dl className="mt-10 max-w-xl space-y-8 text-base leading-7 text-gray-300 lg:max-w-none">
                            {features.map((feature) => (
                                <div key={feature.name} className="relative pl-9">
                                    <dt className="inline font-semibold text-white">
                                        <feature.icon aria-hidden="true" className="absolute left-1 top-1 h-5 w-5 text-indigo-400" />
                                        {feature.name}
                                    </dt>{' '}
                                    <dd className="inline">{feature.description}</dd>
                                </div>
                            ))}
                        </dl>
                    </div>
                </div>
            </div>
        </div>
    )
}