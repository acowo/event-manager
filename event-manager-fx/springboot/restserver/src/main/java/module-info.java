module eventmanager.springboot.restserver {
  requires eventmanager.core;
    requires spring.web;
    requires spring.context;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;

    exports springboot.restserver;
}
