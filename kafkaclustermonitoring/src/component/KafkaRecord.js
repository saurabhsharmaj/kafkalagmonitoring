import React, {useState,useEffect} from "react";
import './KafkaRecord.css';
// import NavBar from "./Navbar";
import applicationService from '../sevices/application.service';
import { Link} from 'react-router-dom';
import { FaEdit, FaTrash, FaPlus } from "react-icons/fa";
import NavBar from "./Navbar";

const ClusterInfomation = () => {

    const [ClusterRecord, setClusterRecord] = useState([]);
  
    const init = () => {
      applicationService.getAll()
        .then(response => {
          console.log('Printing Cluster data', response.data);
          setClusterRecord(response.data);
        })
        .catch(error => {
          console.log('Something went wrong', error);
        }) 
    }
  
    useEffect(() => {
      init();
    }, []);

    // const handleTopic = (clusterid) => {
    //     console.log('Printing id', clusterid);
    //     if(window.confirm("Do you want to...??")){
    //         applicationService.gettopiccluster(clusterid)
    //         .then(response => {
    //         console.log('printing data', response.data);           
    //       })
    //       .catch(error => {
    //         console.log('Something went wrong', error);
    //       })
    //     }
    // }    
    // const handleDelete = (clusterid) => {
    //     console.log('Printing id', clusterid);
    //     if(window.confirm("Do you want to delete...??")){
    //         applicationService.removecluster(clusterid)
    //         .then(response => {
    //         console.log(response.data);
    //         init();            
    //       })
    //       .catch(error => {
    //         console.log('Something went wrong', error);
    //       })
    //     }
    // }

    const handleDelete = (clusterid) => {
        console.log('Printing id', clusterid);
        if(window.confirm("Do you want to delete...??")){
            applicationService.remove(clusterid)
            .then(response => {
            console.log(response.data);
            init();            
          })
          .catch(error => {
            console.log('Something went wrong', error);
          })
        }
    }

    return(
        <>
        <NavBar/>
        <React.Fragment>

            <div className="container">
                <div className="row">
                    <table className="table border">
                        <thead>
                            <tr>
                                <th>ID</th>                                        
                                <th>Cluster Name</th>                                        
                                <th>Monitoring Status</th>                                                                               
                                <th>Zookeeper Server</th>
                                <th>Zookeeper Log Directory</th>
                                <th>Broker Server</th>
                                <th>Broker Log Directory</th>
                                <th>Action</th>                                               
                            </tr>
                        </thead>

                        <tbody>
                            {
                                ClusterRecord.map(cluster => (
                                    <tr key={cluster.clusterid}>
                                        <td>
                                            {cluster.clusterid}
                                            <Link to={`/cluster/edit/${cluster.clusterid}`}>
                                                <FaEdit className='icon_click'/>
                                            </Link>                                                     
                                        </td>

                                        <td>
                                        
                                            <Link to = {`http://${cluster.kafdropPort}`} className="kafdrop-link" target="_blank">{cluster.clustername}</Link>
                                            {/* <Link to={`http://localhost:8081/api/gettopicdata/${cluster.clusterid}`}>
                                                <FaEye className='icon_click' onClick={() => {
                                                    // handleTopic(cluster.clusterid);
                                                }}/>
                                            </Link>                                                     */}
                                        </td>
                                        <td>{(cluster.monitoringstatus === 1) ? "Enable" : "Disable"}</td>
                                        <td>{cluster.zookeeper_servers}</td>
                                        <td>{cluster.zoo_logs_dir}</td>
                                        <td>{cluster.bootstrap_servers}</td>
                                        <td>{cluster.broker_logs_dir}</td>
                                        <td>
                                            <Link to>
                                                    <FaTrash className='icon_click' onClick={() => {
                                                        handleDelete(cluster.clusterid);
                                                        }}/>
                                            </Link>
                                        </td>
                                    </tr>
                                ))
                            }                                   
                        </tbody>
                    </table>
                    <Link to = '/cluster' className='add-cluster'><FaPlus/>Add Cluster</Link>
                </div>
            </div>
            
        </React.Fragment>
        </>
    )
}

export default ClusterInfomation;
