module eventmanager.core {
    exports event;
    exports user;

    requires persistence;

    opens event;
    opens user;
}
