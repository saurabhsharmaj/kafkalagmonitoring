import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/getclusterdetails');
}

const create = data => {
    return httpClient.post("/postclusterdetails", data);
}

const get = id => {
    return httpClient.get(`/cluster/${id}`);
}

const update = data => {
    return httpClient.put('/updateclusterdetails', data);
}

const remove = id => {
    return httpClient.delete(`/deletetopic/${id}`);
}

// const getTopic =id=>  {
//     return httpClient.get(`/gettopicdetails/${id}`);
// }

const getTopic = () => {
    return httpClient.get('/gettopicdetails');
}

const gettopiccluster = id => {
    return httpClient.get(`/gettopicdata/${id}`);
}

// export default { getAll, create, get, update, remove };
export default { getAll, remove, update, create, get, getTopic,gettopiccluster};