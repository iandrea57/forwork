namespace java com.mmz.xt.service.api.model
namespace cpp xt.service.api.model

enum Type {
    ALL = 0,
    SUCCESS = 1,
    FAILED = 2
}

struct Info {
    1: required i32 id;
    2: optional i64 seq;
    3: optional string name;
    4: optional double money;
    5: optional Type type;
}
