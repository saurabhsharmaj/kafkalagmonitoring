import React from 'react';
import './Navbar.css';
import { Link } from 'react-router-dom';
// import MyImage from 'C:/Users/DELL/Desktop/Kafka-Tools.png';

const NavBar = () => {
  return (
    <>
    <nav className="navbar">
    <h2>Kafka Monitoring Tool</h2>
      {/* <img src={'http://localhost:3000/kmt-logo.png'}/> */}
      <ul className="nav-links">
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
    {/* <img src={MyImage} alt="C:/Users/DELL/Desktop/React-icon.png" className='myImage' /> */}
    
    </>
  );
};

export default NavBar