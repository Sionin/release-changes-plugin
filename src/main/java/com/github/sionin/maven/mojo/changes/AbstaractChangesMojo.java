/****************************************************************************\
 __FILE..........: ChangesMojoTest.java
 __AUTHOR........: sergei

 __VERSION.......: 1.0
 __DESCRIPTION...:
 __HISTORY.......: DATE       COMMENT
 _____________________________________________________________________________
 ________________:10.01.13 sergei: created.
 ****************************************************************************/


package com.github.sionin.maven.mojo.changes;

import org.apache.maven.plugin.AbstractMojo;

import java.io.File;

public abstract class AbstaractChangesMojo extends AbstractMojo {

    /*===========================================[ STATIC VARIABLES ]===============*/

    /*===========================================[ INSTANCE VARIABLES ]=============*/

    /**
     * Title
     *
     * @parameter expression="${changes.title}"
     * default-value="${project.name} deployed"
     */
    protected String title;

    /**
     * Load message body from file
     *
     * @parameter expression="${changes.file}"
     * default-value="changes.txt"
     * @required
     */
    protected File changesFile;

    /*===========================================[ CONSTRUCTORS ]===================*/

    /*===========================================[ CLASS METHODS ]==================*/

    /*===========================================[ GETTERS & SETTERS ]==============*/

}
