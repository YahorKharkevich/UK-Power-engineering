import React, { useEffect, useState } from 'react';
import { Chart } from 'react-google-charts';
import { avgEnergyMix, Entry } from '../api';

const EnergyMixCharts: React.FC = () => {
    const [data, setData] = useState<Entry[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const result = await avgEnergyMix();
                setData(result);
            } catch (error) {
                console.error("Error fetching energy mix data:", error);
            }
        };
        fetchData();
    }, []);

    if (data.length === 0) {
        return <div>Loading charts...</div>;
    }

    const colors = [
        '#97e00e', '#3a3433', '#6ed9ec', '#afacaa', '#f6c7b6',
        '#6c3111', '#ffdb00', '#9f27f3', '#0d9bac'
    ];

    return (
        <div className='container'>
            {data.map((entry, index) => {
                const chartData = [
                    ['Component', 'Value'],
                    ['biomass', entry.data.biomass],
                    ['other', entry.data.other],
                    ['hydro', entry.data.hydro],
                    ['imports', entry.data.imports],
                    ['gas', entry.data.gas],
                    ['coal', entry.data.coal],
                    ['solar', entry.data.solar],
                    ['nuclear', entry.data.nuclear],
                    ['wind', entry.data.wind]
                ];

                const options = {
                    title: `${entry.from.substring(0, 10)} - ${entry.to.substring(0, 10)}`,
                    legend: { position: 'none' },
                    colors: colors,
                    pieHole: 0.4,
                };

                return (
                    <div className='item' key={index}>
                        <div className='chart'>
                            <Chart
                                chartType="PieChart"
                                width="100%"
                                height="400px"
                                data={chartData}
                                options={options}
                            />
                        </div>
                        <div className='label-cont'>
                            <label className="label">
                                {entry["clean energy percent"].toFixed(2)}%
                            </label>
                        </div>
                    </div>
                );
            })}
        </div>
    );
};

export default EnergyMixCharts;
