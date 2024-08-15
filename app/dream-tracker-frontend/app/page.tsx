import Link from "next/link";
import { Button } from "@/components/ui/button"

export default function Home() {
    return (
        <main className="p-4">
            <h1>PANEL</h1>
            <div>

                <Link href="/users" className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-700 transition duration-300">
                    Add User
                </Link>
                <Link href="/goals" className="px-4 py-2 bg-red-500 text-white rounded hover:bg-green-700 transition duration-300">
                   goals
                </Link>
                <Link href="/categories" className="px-4 py-2 bg-pink-950 text-white rounded hover:bg-green-700 transition duration-300">
                   categories
                </Link>
                <Button href = "">
                    <Link href="/habits">
                        Habits
                    </Link>
                </Button>
            </div>
        </main>
    )
}
