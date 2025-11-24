import React, {Suspense, useEffect, useState} from 'react';
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
                alert("Error fetching energy mix data!");
            }
        };
        fetchData();
    }, []);

    if (data.length === 0) {
        return <div className='Loading'>Loading...</div>;
    }

    const colors = [
        '#c701ff', '#4ecb8d', '#008dff', '#0d9bac', '#ff73b6',
        '#ff9d3a', '#f9e858', '#d83034', '#003a7d'
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
                    title: `${index === 0 ? "Today" : index === 1 ? "Tomorrow" : "The day after Tomorrow"} `,
                    titleFontSize: 15,
                    legend: { position: 'bottom' },
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
                            <h4>Clean energy percent:</h4>
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
