package com.example.ktornotescompose.util


import kotlinx.coroutines.flow.*

//compiler keyword takes code and puts it into place
inline fun <ResultType, RequestType> networkBoundResource(
    //crossinline to use lambda inside of inline fun
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType, // network response
    //used after response to insert data into database
    crossinline saveFetchResult: suspend(RequestType) -> Unit,
    //what to do if request fails
    crossinline onFetchFailed: (Throwable) -> Unit = { Unit },
    //determine if you want to fetch data - by default always fetch
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow<Resource<ResultType>> {
    emit(Resource.Loading(null))
    val data = query().first()
    val flow = if (shouldFetch(data)) {
        //make the call to the api to refresh the data
        //show cached data from the db before making a call to the api
        emit(Resource.Loading(data))
        try {
            //make request to server
            val fetchedResult = fetch()
            //save the response from server to db
            saveFetchResult(fetchedResult)
            //return new data from the data base wrap in resource
            query().map { Resource.Success(it) }
        } catch (t: Throwable) {
            //react to error
            onFetchFailed(t)
            //return the locally cached data with error + old data
            query().map {
                Resource.Error("couldn't reach server", it)
            }
        }
    } else {
        //dont want to fetch data return cached data + success
        query().map { Resource.Success(it) }
    }
    emitAll(flow)
}