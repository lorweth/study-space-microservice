import useCountDown from 'app/shared/custom-hook/useCountDown';
import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { toast } from 'react-toastify';
import './clock.css';

type PropType = {
  duration: number;
} & RouteComponentProps<{ id: string }>;

const CountDownTimer = (props: PropType) => {
  const { duration, history, match } = props;

  const [second, isFinish] = useCountDown(duration * 60);

  useEffect(() => {
    if (isFinish) {
      toast.success('Bài làm đã kết thúc');
      history.push(`/learning-manager/${match.params.id}`);
    }
  }, [isFinish]);

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
