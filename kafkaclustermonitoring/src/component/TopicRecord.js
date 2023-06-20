import React, { useEffect, useState } from 'react';
import './KafkaRecord.css';
import NavBar from "./Navbar";
import { Link, useParams} from 'react-router-dom';
import { FaTrash,FaEye } from "react-icons/fa";
import applicationService from '../sevices/application.service';
import axios from 'axios';

const TopicList = () => {

    const [TopicRecord, setTopicRecord] = useState([]);
  
    const params = useParams();
    alert(params);
    // const init = clusterid => {
    //     console.log("jiankdsnldsjjf");
    //         applicationService.gettopiccluster(clusterid)
    //         .then(response => {
    //         console.log('Printing Topic data', response.data);  
    //         setTopicRecord(response.data);         0  
    //       })
    //       .catch(error => {
    //         console.log('Something went wrong', error);
    //       })
    // }     
    const init = clusterid => {
      applicationService.getTopic()
        .then(response => {
          console.log('Printing Topic data', response.data);
          setTopicRecord(response.data);
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
    //         console.log(response.data);           
    //       })
    //       .catch(error => {
    //         console.log('Something went wrong', error);
    //       })
    //     }
    // }
        

    useEffect(()=> {
        alert(console.log('hfhsdjfhsdkjhf',params.id));
        axios.get(applicationService.gettopiccluster(`${params.id}`))
        .then(res => {
            console.log(res)
            setTopicRecord(res.data)
        })
        .catch(err =>{
            console.log(err)
        })
    }, [params.id])

    const handleDelete = (groupid) => {
        console.log('Printing id', groupid);
        if(window.confirm("Do you want to delete...??")){
            applicationService.remove(groupid)
            .then(response => {
            console.log(response.data);
//            init();            
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
                            <table className="table table-borderd">
                                <thead>
                                    <tr>
                                        <th>ID</th>                                
                                        <th>Consumer Group</th>  
                                        <th>Topic Name</th>
                                        <th>Owner</th>                              
                                        <th>Email Id</th>                                
                                        <th>Monitoring Status</th>                                
                                        <th>Threshold Value</th>                                
                                        <th>Timestamp</th>                                
                                        <th>Description</th>                                        
                                        <th>Action</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    {
                                        TopicRecord.map(topic => (
                                            <tr key={topic.id}>
                                                <td>{topic.groupid}</td>
                                                <td>{topic.consumergroup}</td>
                                                <td>{topic.topicname}</td>
                                                <td>{topic.owner}</td>
                                                <td>{topic.emailid}</td>
                                                <td>{topic.monitoringstatus}</td>
                                                <td>{topic.threshold}</td>
                                                <td>{topic.timestamp}</td>
                                                <td>{topic.description}</td>
                                                <td>
                                                    <Link to>
                                                    <FaTrash className='icon_click' onClick={() => {
                                                        handleDelete(topic.groupid);
                                                        }}/>
                                                    </Link>
                                                    {/* <Link to>
                                                        <FaEye className='icon_click' onClick={() => {
                                                        handleTopic(topic.clusterid);
                                                        }}/>
                                                    </Link> */}
                                                      {/* <Link to = '/addcluster'>
                                                    <FaEye/>
                                                    </Link>                                                   */}
                                                </td>
                                            </tr>
                                        ))
                                    }                                   
                                </tbody>
                            </table>
                </div>
            </div>
        </React.Fragment>
        </>
    )
}

export default TopicList
