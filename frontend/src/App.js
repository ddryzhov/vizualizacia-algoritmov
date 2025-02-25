import React from 'react';
import { HashRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import GrammarAnalysis from './components/grammar/GrammarAnalysis';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/analysis" element={<GrammarAnalysis />} />
                <Route path="/" element={<Navigate to="/analysis" />} />
            </Routes>
        </Router>
    );
}

export default App;
