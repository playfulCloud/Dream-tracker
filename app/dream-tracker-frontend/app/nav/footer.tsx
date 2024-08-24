import { FaGithub, FaLinkedin, FaHeart } from "react-icons/fa";

export function SiteFooter() {
    return (
        <footer className="py-6 md:px-8 md:py-0 bg-gray-800 text-white">
            <div className="container flex flex-col items-center justify-center gap-4 md:h-24 md:flex-row">
                <p className="text-balance text-center text-sm leading-loose text-gray-400 md:text-left">
                    Made with{" "}
                    <FaHeart className="inline text-red-500" />{" "}
                    by{" "}
                    <a
                        href=""
                        target="_blank"
                        rel="noreferrer"
                        className="font-medium underline underline-offset-4 text-white"
                    >
                        playfulCloud
                    </a>
                    . Check out my{" "}
                </p>
                <div className="flex gap-4">
                    <a
                        href="https://github.com/playfulCloud"
                        target="_blank"
                        rel="noreferrer"
                        className="text-white hover:text-gray-400"
                    >
                        <FaGithub size={24} />
                    </a>
                    <a
                        href="https://www.linkedin.com/in/jakub-ptaszkowski-bb8551287/"
                        target="_blank"
                        rel="noreferrer"
                        className="text-white hover:text-gray-400"
                    >
                        <FaLinkedin size={24} />
                    </a>
                </div>
            </div>
        </footer>
    );
}

