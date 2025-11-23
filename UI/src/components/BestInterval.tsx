import React, { useState } from 'react';
import { bestInterval, Data } from '../api';

const BestInterval: React.FC = () => {
    const [duration, setDuration] = useState<number | ''>('');
    const [result, setResult] = useState<Data | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleCalculate = async () => {
        const val = Number(duration);
        if (val < 1 || val > 6) {
            alert('Not valid input');
            return;
        }
        try {
            setError(null);
            const res = await bestInterval(val);
            setResult(res);
        } catch (err) {
            console.error(err);
            setError("Failed to fetch data");
        }
    };

    return (
        <div>
            <div id="cont1" style={{ justifyContent: 'center' }}>
                <input
                    id="input1"
                    placeholder="Number of hours"
                    className="input"
                    name="text"
                    type="number"
                    value={duration}
                    onChange={(e) => setDuration(Number(e.target.value))}
                />
                <button className="btn" id="btn2" onClick={handleCalculate}>Try</button>
            </div>

            <div id="cont2">
                <label id="label1" className="label">
                    {result ? result.from : "From date"}
                </label>
                <label id="label2" className="label">
                    {result ? result.to : "To date"}
                </label>
                <label id="label3" className="label">
                    {result ? `${result["clean energy percent"].toFixed(2)}%` : "Clean energy percent"}
                </label>
            </div>
            {error && <div style={{ color: 'red', textAlign: 'center' }}>{error}</div>}
        </div>
    );
};

export default BestInterval;
