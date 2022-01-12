import { useEffect, useState } from 'react';

const useCountDown = (second: number) => {
  const [timeLeft, setTimeLeft] = useState(second);
  const [isFinish, setIsFinish] = useState(false);

  useEffect(() => {
    if (!timeLeft) return setIsFinish(true);

    const intervalID = setInterval(() => setTimeLeft(timeLeft - 1), 1000);

    return () => clearInterval(intervalID);
  }, [timeLeft]);

  return [timeLeft, isFinish] as const;
};

export default useCountDown;
