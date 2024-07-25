import Image from "next/image";
import Link from "next/link";

export default function Home() {
  return (
      <main><h1>Habit related things</h1>
      <Link href="/habits">Habits</Link>
          <Link href="/users">add user</Link>
      </main>
  )
}
