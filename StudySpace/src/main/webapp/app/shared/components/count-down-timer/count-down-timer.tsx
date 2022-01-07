import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import './clock.css';

type PropType = {
  duration: number;
};

const CountDownTimer = (props: PropType) => {
  const { duration } = props;

  const [second, setSecond] = useState(duration * 60);

  useEffect(() => {
    const interval = setInterval(() => {
      if (second > 0) {
        setSecond(s => s - 1);
      } else {
        clearInterval(interval);
        toast.error('Time out');
      }
    }, 1000);
  }, [duration]);

  const displayTime = () => {
    const hour = Math.floor(second / 3600);
    const minutes = Math.floor((second % 3600) / 60);
    const seconds = second % 60;
    return `${hour}:${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
  };

  return (
    <div className="clock">
      <p>{displayTime()}</p>
    </div>
  );
};
export default CountDownTimer;
