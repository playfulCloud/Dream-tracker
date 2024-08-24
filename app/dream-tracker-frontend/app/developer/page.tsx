"use client";
export default function AboutMe() {
    return (
        <div className="bg-gray-900 min-h-screen flex items-center justify-center">
            <div className="mx-auto max-w-7xl py-24 sm:px-6 sm:py-32 lg:px-8">
                <div className="relative isolate overflow-hidden bg-gray-800 px-6 pt-16 shadow-2xl sm:rounded-3xl sm:px-16 md:pt-24 lg:flex lg:items-center lg:px-24 lg:pt-0">
                    <svg
                        viewBox="0 0 1024 1024"
                        aria-hidden="true"
                        className="absolute left-1/2 top-1/2 -z-10 h-[64rem] w-[64rem] -translate-y-1/2 [mask-image:radial-gradient(closest-side,white,transparent)] sm:left-full sm:-ml-80 lg:left-1/2 lg:ml-0 lg:-translate-x-1/2 lg:translate-y-0"
                    >
                        <circle r={512} cx={512} cy={512} fill="url(#gradient)" fillOpacity="0.7" />
                        <defs>
                            <radialGradient id="gradient">
                                <stop stopColor="#1f1f3a" />
                                <stop offset={1} stopColor="#2d2d6c" />
                            </radialGradient>
                        </defs>
                    </svg>
                    <div className="lg:flex lg:items-center lg:justify-between lg:w-full">
                        <div className="max-w-md text-center lg:text-left lg:max-w-lg">
                            <h2 className="text-3xl font-bold tracking-tight text-white sm:text-4xl">
                                About Me
                            </h2>
                            <p className="mt-6 text-lg leading-8 text-gray-300">
                                Hi, I'm Jacob, I like weird split ergonomic keyboards like Kinesis, Arch linux(i3 of course) and grinding my craft which is programming.
                            </p>
                            <p className="mt-4 text-lg leading-8 text-gray-300">
                                Enjoy using dream-tracker I hope it will help you as much as much as me. Practice reigns Supreme.
                            </p>
                        </div>
                        <div className="relative mt-16 lg:mt-0 lg:ml-10 lg:flex-shrink-0">
                            <img
                                alt="Your portrait"
                                src="/me.jpg"
                                className="w-96 h-96 rounded-xl bg-white/5 ring-1 ring-white/10 object-cover"
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

