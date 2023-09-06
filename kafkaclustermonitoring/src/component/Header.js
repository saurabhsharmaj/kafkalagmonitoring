import React from 'react';
import './Header.css';
import { Link } from 'react-router-dom';

const TopHeader = () => {
  return (
    <>
    <nav className="navbar">
    <h2>Kafka Monitoring Tool</h2>
      {/* <img src={'http://192.168.1.30:3000/kmt-logo.png'}/> */}
      <ul className="nav-links">

        <li>
          <Link to = '/'>Home</Link>
        </li>

        <li>
          <Link to = '/clusterinfo'>Cluster List</Link>
        </li>

        <li>
          <Link to = '/topic'>Topic Record</Link>
        </li>
{/* 
        <li>
          <Link to = '/kafka'>Add Kafka Topic</Link>
        </li> */}

        {/* <li>
          <Link to = '/cluster'>Add Cluster Information</Link>
        </li> */}

        {/* <li>
          <Link to = '/addcluster'>Cluster Information</Link>
        </li> */}

      </ul>       
    </nav>
    </>
  );
};

export default TopHeader