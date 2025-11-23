import React from 'react';
import EnergyMixCharts from './components/EnergyMixCharts';
import BestInterval from './components/BestInterval';
import './style.css';

const App: React.FC = () => {
    return (
        <div>
            <EnergyMixCharts />
            <BestInterval />
        </div>
    );
};

export default App;
