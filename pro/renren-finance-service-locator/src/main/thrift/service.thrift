namespace java com.mmz.service.api
namespace cpp mmz.service.api

include "model/info.thrift"

struct GetInfoRequest {
    1: required i32 uid;
}

struct GetInfoResponse {
    1: optional info.Info info;

}

service InfoService {
    GetInfoResponse getInfo(1: GetInfoRequest request);
}