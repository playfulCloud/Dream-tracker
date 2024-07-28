import Image from "next/image";
import Link from "next/link";

export default function Home() {
    return (
        <main className="p-4">
            <h1 className="text-black-2xl font-bold mb-4">PANEL</h1>
            <div className="flex space-x-4">
                <Link href="/habits" className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700 transition duration-300">
                    Habits
                </Link>
                <Link href="/users" className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-700 transition duration-300">
                    Add User
                </Link>
                <Link href="/goals" className="px-4 py-2 bg-red-500 text-white rounded hover:bg-green-700 transition duration-300">
                   goals
                </Link>
                <Link href="/categories" className="px-4 py-2 bg-pink-950 text-white rounded hover:bg-green-700 transition duration-300">
                   categories
                </Link>

            </div>
        </main>
    )
}
