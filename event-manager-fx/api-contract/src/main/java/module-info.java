module api.contract {
    requires transitive eventmanager.core;

    exports dto.request;
    exports dto;

    opens dto;
    opens dto.request;
}