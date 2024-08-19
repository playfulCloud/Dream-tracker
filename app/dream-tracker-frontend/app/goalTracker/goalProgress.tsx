import * as React from "react";
import { Progress } from "@/components/ui/progress";

interface ProgressDemoProps {
  completionCount: number;
  currentCount: number;
}

export function ProgressDemo({ completionCount, currentCount }: ProgressDemoProps) {
  const progress = (currentCount / completionCount) * 100;

  return <Progress value={progress} className="w-[100%]" />;
}
