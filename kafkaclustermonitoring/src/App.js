import {Routes , Route } from "react-router-dom";
import NavBar from "./component/Navbar";
import KafkaInfo from "./component/KafkaEntityDetails";
// import Kafkalist from "./component/KafkaRecord";
import Clusterinfo from "./component/ClusterInfo";
import Topiclist from "./component/TopicRecord";
import AddCluster from "./component/AddCluster";
import ClusterInfomation from "./component/KafkaRecord";
import CustomerUploadForm from "./component/KafkaEntityUploadForm";


function App(){
      return (
        <>
          <Routes>
            <Route path="/" Component={NavBar}/>
            <Route path="/clusterinfo" Component={ClusterInfomation}/>
            <Route path="/kafka" Component={KafkaInfo}/>
            <Route path="/cluster" Component={Clusterinfo}/>
            <Route path="/cluster/edit/:clusterid" Component={Clusterinfo}/>
            <Route path="/topic" Component={Topiclist}/>
            <Route path ="/kafka/edit/:groupid" Component={KafkaInfo}/>
            <Route path="/addcluster" Component={AddCluster}/>
            <Route path="/CustomerUploadForm" Component={CustomerUploadForm}/>   
            <Route path="api/gettopicdata/:id" Component={Topiclist}/>
          </Routes>
        </>
      );
    }
    
    export default App;