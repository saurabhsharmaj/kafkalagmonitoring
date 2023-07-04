import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/getclusterdetails');
}

const get = id => {
    return httpClient.get(`/cluster/${id}`);
}

const getTopic = () => {
    return httpClient.get('/gettopicdetails');
}

const gettopiccluster = id => {
    return httpClient.get(`/gettopicdata/${id}`);
}

const getTopicById =id=>  {
    return httpClient.get(`/kafkadetails/${id}`);
}

const create = data => {
    return httpClient.post("/postclusterdetails", data);
}

const post = data => {
   return httpClient.post("/post/kafka", data);
}


const update = data => {
    return httpClient.put('/updateclusterdetails', data);
}

const updateKafka = data => {
    return httpClient.put('/kafkadetailsupdate',data);
}

const remove = id => {
    return httpClient.delete(`/clusterdelete/${id}`);
}

const removetopic = id => {
    return httpClient.delete(`/deletetopic/${id}`);
}

// export default { getAll, create, get, update, remove };
export default { getAll, remove, update, create, get, getTopicById, gettopiccluster, post, removetopic, updateKafka, getTopic};