import React, { useState } from 'react';
import { bestInterval, Data } from '../api';

const BestInterval: React.FC = () => {
    const [duration, setDuration] = useState<number | ''>('');
    const [result, setResult] = useState<Data | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleChange = (e) => {
        setDuration(e.target.value)
    }

    const handleCalculate = async () => {
        const val = Number(duration);
        if (val < 1 || val > 6) {
            alert('Not valid input');
            return;
        }
        try {
            setError(null);
            const res = await bestInterval(val);

            const convert = (value) => `${Math.floor(value.getHours()/10)}${value.getHours()%10}:${Math.floor(value.getMinutes()/10)}${value.getMinutes()%10} ${value.getDate()}.${value.getMonth()+1}.${value.getFullYear()}`;

            const oldFrom = new Date(res.from);
            res.from = convert(oldFrom)

            const oldTo = new Date(res.to);
            res.to = convert(oldTo)

            setResult(res);
        } catch (err) {
            setError("Failed to fetch data");
        }
    };

    return (
        <div className='small-container'>
            <div id="cont1">
                <input
                    id="input1"
                    placeholder="Number of hours"
                    className="input"
                    name="text"
                    type="number"
                    value={duration}
                    onChange={handleChange}
                />
                <button className="btn" id="btn2" onClick={handleCalculate}>Try</button>
            </div>

            <div id="cont2">
                <div>
                    <h4>From date:</h4>
                    <label id="label1" className="label">
                        {result ? result.from : "..."}
                    </label>
                </div>
                <div>
                    <h4>To date:</h4>
                    <label id="label2" className="label">
                        {result ? result.to : "..."}
                    </label>
                </div>
                <div>
                    <h4>Clean energy percent:</h4>
                    <label id="label3" className="label">
                        {result ? `${result["clean energy percent"].toFixed(2)}%` : "..."}
                    </label>
                </div>
            </div>
            {error && <div style={{ color: 'red', textAlign: 'center' }}>{error}</div>}
        </div>
    );
};

export default BestInterval;
