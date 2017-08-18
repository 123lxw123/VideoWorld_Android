package com.xunlei.downloadlib.parameter;

public class XLConstant {

    public static class QuickInfoState {
        public static final int QI_FAILED = 3;
        public static final int QI_FINISH = 2;
        public static final int QI_STOP = 0;
        public static final int QI_TRY = 1;
    }

    public enum XLCreateTaskMode {
        NEW_TASK,
        CONTINUE_TASK
    }

    public enum XLDownloadHeaderState {
        GDHS_UNKOWN,
        GDHS_REQUESTING,
        GDHS_SUCCESS,
        GDHS_ERROR
    }

    public static class XLErrorCode {
        public static final int ADD_RESOURCE_ERROR = 9122;
        public static final int ALREADY_INIT = 9101;
        public static final int APPKEY_CHECKER_ERROR = 9901;
        public static final int APPNAME_APPKEY_ERROR = 9116;
        public static final int ASYN_FILE_E_BASE = 111300;
        public static final int ASYN_FILE_E_EMPTY_FILE = 111305;
        public static final int ASYN_FILE_E_FILE_CLOSING = 111308;
        public static final int ASYN_FILE_E_FILE_NOT_OPEN = 111303;
        public static final int ASYN_FILE_E_FILE_REOPEN = 111304;
        public static final int ASYN_FILE_E_FILE_SIZE_LESS = 111306;
        public static final int ASYN_FILE_E_OP_BUSY = 111302;
        public static final int ASYN_FILE_E_OP_NONE = 111301;
        public static final int ASYN_FILE_E_TOO_MUCH_DATA = 111307;
        public static final int BAD_DIR_PATH = 111083;
        public static final int BT_SUB_TASK_NOT_SELECT = 9306;
        public static final int BUFFER_OVERFLOW = 111039;
        public static final int COMMON_ERRCODE_BASE = 111024;
        public static final int CONF_MGR_ERRCODE_BASE = 111159;
        public static final int CONTINUE_NO_NAME = 9115;
        public static final int CORRECT_CDN_ERROR = 111180;
        public static final int CORRECT_TIMES_TOO_MUCH = 111179;
        public static final int CREATE_FILE_FAIL = 111139;
        public static final int CREATE_THREAD_ERROR = 9117;
        public static final int DATA_MGR_ERRCODE_BASE = 111119;
        public static final int DISK_FULL = 9110;
        public static final int DISPATCHER_ERRCODE_BASE = 111118;
        public static final int DNS_INVALID_ADDR = 111078;
        public static final int DNS_NO_SERVER = 111077;
        public static final int DOWNLOAD_MANAGER_ERROR = 9900;
        public static final int DYNAMIC_PARAM_FAIL = 9114;
        public static final int ERROR_INVALID_INADDR = 111050;
        public static final int ERR_DPLAY_ALL_SEND_COMPLETE = 118000;
        public static final int ERR_DPLAY_BROKEN_SOCKET_RECV = 118307;
        public static final int ERR_DPLAY_BROKEN_SOCKET_SEND = 118306;
        public static final int ERR_DPLAY_CLIENT_ACTIVE_DISCONNECT = 118001;
        public static final int ERR_DPLAY_DO_DOWNLOAD_FAIL = 118305;
        public static final int ERR_DPLAY_DO_READFILE_FAIL = 118311;
        public static final int ERR_DPLAY_EV_SEND_TIMTOUT = 118310;
        public static final int ERR_DPLAY_HANDLE_DOWNLOAD_FAILED = 118302;
        public static final int ERR_DPLAY_NOT_FOUND = 118005;
        public static final int ERR_DPLAY_PLAY_FILE_NOT_EXIST = 118304;
        public static final int ERR_DPLAY_RECV_STATE_INVALID = 118308;
        public static final int ERR_DPLAY_SEND_FAILED = 118300;
        public static final int ERR_DPLAY_SEND_RANGE_INVALID = 118301;
        public static final int ERR_DPLAY_SEND_STATE_INVALID = 118309;
        public static final int ERR_DPLAY_TASK_FINISH_CANNT_DOWNLOAD = 118004;
        public static final int ERR_DPLAY_TASK_FINISH_CONTINUE = 118003;
        public static final int ERR_DPLAY_TASK_FINISH_DESTROY = 118002;
        public static final int ERR_DPLAY_UNKNOW_HTTP_METHOD = 118303;
        public static final int ERR_INVALID_ADDRESS_FAMILY = 116001;
        public static final int ERR_P2P_ALLOC_MEM_ERR = 11313;
        public static final int ERR_P2P_BROKER_CONNECT = 11308;
        public static final int ERR_P2P_CONNECT_FAILED = 11311;
        public static final int ERR_P2P_CONNECT_UPLOAD_SLOW = 11312;
        public static final int ERR_P2P_HANDSHAKE_RESP_FAIL = 11303;
        public static final int ERR_P2P_INVALID_COMMAND = 11309;
        public static final int ERR_P2P_INVALID_PARAM = 11310;
        public static final int ERR_P2P_NOT_SUPPORT_UDT = 11307;
        public static final int ERR_P2P_REMOTE_UNKNOWN_MY_CMD = 11306;
        public static final int ERR_P2P_REQUEST_RESP_FAIL = 11304;
        public static final int ERR_P2P_SEND_HANDSHAKE = 11314;
        public static final int ERR_P2P_UPLOAD_OVER_MAX = 11305;
        public static final int ERR_P2P_VERSION_NOT_SUPPORT = 11301;
        public static final int ERR_P2P_WAITING_CLOSE = 11302;
        public static final int ERR_PTL_GET_PEERSN_FAILED = 112600;
        public static final int ERR_PTL_PEER_OFFLINE = 112500;
        public static final int ERR_PTL_PROTOCOL_NOT_SUPPORT = 112400;
        public static final int FILE_CANNOT_TRUNCATE = 111084;
        public static final int FILE_CFG_ERASE_ERROR = 111130;
        public static final int FILE_CFG_MAGIC_ERROR = 111131;
        public static final int FILE_CFG_READ_ERROR = 111132;
        public static final int FILE_CFG_READ_HEADER_ERROR = 111134;
        public static final int FILE_CFG_RESOLVE_ERROR = 111135;
        public static final int FILE_CFG_TRY_FIX = 111129;
        public static final int FILE_CFG_WRITE_ERROR = 111133;
        public static final int FILE_CREATING = 111145;
        public static final int FILE_EXISTED = 9109;
        public static final int FILE_INVALID_PARA = 111144;
        public static final int FILE_NAME_TOO_LONG = 9125;
        public static final int FILE_NOT_EXIST = 111143;
        public static final int FILE_PATH_TOO_LONG = 111120;
        public static final int FILE_SIZE_NOT_BELIEVE = 111141;
        public static final int FILE_SIZE_TOO_SMALL = 111142;
        public static final int FILE_TOO_BIG = 111086;
        public static final int FIL_INFO_INVALID_DATA = 111146;
        public static final int FIL_INFO_RECVED_DATA = 111147;
        public static final int FULL_PATH_NAME_OCCUPIED = 9128;
        public static final int FULL_PATH_NAME_TOO_LONG = 9127;
        public static final int FUNCTION_NOT_SUPPORT = 9123;
        public static final int HTTP_HUB_CLIENT_E_BASE = 115100;
        public static final int HTTP_SERVER_NOT_START = 9400;
        public static final int INDEX_NOT_READY = 9303;
        public static final int INSUFFICIENT_DISK_SPACE = 111085;
        public static final int INVALID_ARGUMENT = 111041;
        public static final int INVALID_ITERATOR = 111038;
        public static final int INVALID_SOCKET_DESCRIPTOR = 111048;
        public static final int INVALID_TIMER_INDEX = 111074;
        public static final int IP6_ERRCODE_BASE = 116000;
        public static final int IP6_INVALID_IN6ADDR = 116002;
        public static final int IP6_NOT_SUPPORT_SSL = 116003;
        public static final int MAP_DUPLICATE_KEY = 111036;
        public static final int MAP_KEY_NOT_FOUND = 111037;
        public static final int MAP_UNINIT = 111035;
        public static final int NET_BROKEN_PIPE = 111170;
        public static final int NET_CONNECTION_REFUSED = 111171;
        public static final int NET_CONNECT_SSL_ERR = 111169;
        public static final int NET_NORMAL_CLOSE = 111175;
        public static final int NET_OP_CANCEL = 111173;
        public static final int NET_REACTOR_ERRCODE_BASE = 111168;
        public static final int NET_SSL_GET_FD_ERROR = 111172;
        public static final int NET_UNKNOWN_ERROR = 111174;
        public static final int NOT_FULL_PATH_NAME = 9404;
        public static final int NOT_IMPLEMENT = 111057;
        public static final int NO_ENOUGH_BUFFER = 9301;
        public static final int NO_ERROR = 9000;
        public static final int ONE_PATH_LEVEL_NAME_TOO_LONG = 9126;
        public static final int OPEN_FILE_ERR = 111128;
        public static final int OPEN_OLD_FILE_FAIL = 111140;
        public static final int OUT_OF_FIXED_MEMORY = 111032;
        public static final int OUT_OF_MEMORY = 111026;
        public static final int P2P_PIPE_ERRCODE_BASE = 11300;
        public static final int PARAM_ERROR = 9112;
        public static final int PAUSE_TASK_WRITE_CFG_ERR = 117000;
        public static final int PAUSE_TASK_WRITE_DATA_TIMEOUT = 117001;
        public static final int PRIOR_TASK_FINISH = 9308;
        public static final int PRIOR_TASK_NO_INDEX = 9307;
        public static final int QUEUE_NO_ROOM = 111033;
        public static final int READ_FILE_ERR = 111126;
        public static final int REDIRECT_TOO_MUCH = 111181;
        public static final int RES_QUERY_E_BASE = 115000;
        public static final int SCHEMA_NOT_SUPPORT = 9113;
        public static final int SDK_NOT_INIT = 9102;
        public static final int SETTINGS_ERR_CFG_FILE_NOT_EXIST = 111162;
        public static final int SETTINGS_ERR_INVALID_FILE_NAME = 111161;
        public static final int SETTINGS_ERR_INVALID_ITEM_NAME = 111164;
        public static final int SETTINGS_ERR_INVALID_ITEM_VALUE = 111165;
        public static final int SETTINGS_ERR_INVALID_LINE = 111163;
        public static final int SETTINGS_ERR_ITEM_NOT_FOUND = 111167;
        public static final int SETTINGS_ERR_LIST_EMPTY = 111166;
        public static final int SETTINGS_ERR_UNKNOWN = 111160;
        public static final int TARGET_THREAD_STOPING = 111025;
        public static final int TASK_ALREADY_EXIST = 9103;
        public static final int TASK_ALREADY_RUNNING = 9106;
        public static final int TASK_ALREADY_STOPPED = 9105;
        public static final int TASK_CONTROL_STRATEGY = 9501;
        public static final int TASK_FAILURE_ALL_SUBTASK_FAILED = 114009;
        public static final int TASK_FAILURE_BTHUB_NO_RECORD = 114008;
        public static final int TASK_FAILURE_CANNOT_START_SUBTASK = 114003;
        public static final int TASK_FAILURE_EMULE_NO_RECORD = 114101;
        public static final int TASK_FAILURE_GET_TORRENT_FAILED = 114006;
        public static final int TASK_FAILURE_NO_DATA_PIPE = 111136;
        public static final int TASK_FAILURE_PARSE_TORRENT_FAILED = 114005;
        public static final int TASK_FAILURE_PART_SUBTASK_FAILED = 114011;
        public static final int TASK_FAILURE_QUERY_BT_HUB_FAILED = 114004;
        public static final int TASK_FAILURE_QUERY_EMULE_HUB_FAILED = 114001;
        public static final int TASK_FAILURE_SAVE_TORRENT_FAILED = 114007;
        public static final int TASK_FAILURE_SUBTASK_FAILED = 114002;
        public static final int TASK_FAILURE_THEONLY_SUBTASK_FAILED = 114010;
        public static final int TASK_FAIL_LONG_TIME_NO_RECV_DATA = 111176;
        public static final int TASK_FILE_NAME_EMPTY = 9401;
        public static final int TASK_FILE_NOT_VEDIO = 9402;
        public static final int TASK_FILE_SIZE_TOO_LARGE = 111177;
        public static final int TASK_FINISH = 9118;
        public static final int TASK_NOT_EXIST = 9104;
        public static final int TASK_NOT_IDLE = 9120;
        public static final int TASK_NOT_RUNNING = 9119;
        public static final int TASK_NOT_START = 9107;
        public static final int TASK_NO_FILE_NAME = 9129;
        public static final int TASK_NO_INDEX_NO_ORIGIN = 111148;
        public static final int TASK_ORIGIN_NONEXISTENCE = 111149;
        public static final int TASK_RETRY_ALWAY_FAIL = 111178;
        public static final int TASK_STILL_RUNNING = 9108;
        public static final int TASK_TYPE_NOT_SUPPORT = 9121;
        public static final int TASK_UNKNOWN_ERROR = 9403;
        public static final int TASK_USE_TOO_MUCH_MEM = 111031;
        public static final int THUNDER_URL_PARSE_ERROR = 9305;
        public static final int TOO_MUCH_TASK = 9111;
        public static final int TORRENT_IMCOMPLETE = 9304;
        public static final int TORRENT_PARSE_ERROR = 9302;
        public static final int URL_IS_TOO_LONG = 111047;
        public static final int URL_PARSER_ERROR = 111046;
        public static final int VIDEO_CACHE_FINISH = 9410;
        public static final int WRITE_FILE_ERR = 111127;
    }

    public enum XLManagerStatus {
        MANAGER_UNINIT,
        MANAGER_INIT_FAIL,
        MANAGER_RUNNING
    }

    public enum XLNetWorkCarrier {
        NWC_Unknow,
        NWC_CMCC,
        NWC_CU,
        NWC_CT
    }

    public static class XLOriginResState {
        public static final int ORIGIN_RES_CHECKING = 1;
        public static final int ORIGIN_RES_DEATH_LINK = 3;
        public static final int ORIGIN_RES_UNUSED = 0;
        public static final int ORIGIN_RES_VALID_LINK = 2;
    }

    public enum XLQueryIndexStatus {
        QIS_UNQUERY,
        QIS_QUERYING,
        QIS_QUERY_HAVE_INFO,
        QIS_QUERY_HAVENT_INFO
    }

    public enum XLResStrategy {
        RUS_PRIOR_USE
    }

    public static class XLTaskStatus {
        public static final int TASK_FAILED = 3;
        public static final int TASK_IDLE = 0;
        public static final int TASK_RUNNING = 1;
        public static final int TASK_STOPPED = 4;
        public static final int TASK_SUCCESS = 2;
    }

    public static class XLTaskType {
        public static final int P2SP_TASK_TYP = 1;
    }
}
