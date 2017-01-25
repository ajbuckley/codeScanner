CREATE DATABASE IF NOT EXISTS codescanner;
USE codescanner;


/* Create blank tables */
CREATE TABLE IF NOT EXISTS sessions(
	UUID                SERIAL,
	startTime            VARCHAR(200) DEFAULT '',
	totalFiles          INT unsigned,
	filesComplete       INT unsigned,
	PRIMARY KEY (UUID)
) ENGINE=MyISAM;

CREATE TABLE IF NOT EXISTS files(
	sessionID           INT unsigned,
	UUID                SERIAL,
	repoID              INT unsigned,
	fileName            VARCHAR(256) DEFAULT '',
	filePath            VARCHAR(512) DEFAULT '',
	PRIMARY KEY (UUID)
) ENGINE=MyISAM; 

CREATE TABLE IF NOT EXISTS candidates(
	sessionID           INT unsigned,
	UUID                SERIAL,
	associatedFile      INT unsigned,
	license             VARCHAR(100) DEFAULT '',
	repoURL             VARCHAR(256) DEFAULT '',
	fileName            VARCHAR(256) DEFAULT '',
	filePath            VARCHAR(512) DEFAULT '',
	PRIMARY KEY (UUID)
) ENGINE=MyISAM; 

CREATE TABLE IF NOT EXISTS results(
	sessionID           INT unsigned,
	UUID                SERIAL,
	sourceFileID        INT unsigned,
	candidateID         INT unsigned,
	matchRating			FLOAT(4,3),
	sourceStartLine     SMALLINT unsigned,
	candidateStartLine  SMALLINT unsigned,
	length              SMALLINT unsigned,
	sourceFileName		VARCHAR(256) DEFAULT '',
	candidateFileName	VARCHAR(256) DEFAULT '',
	candidateUrl		VARCHAR(256) DEFAULT '',
	isOkay              TINYINT(1) DEFAULT 0,
	PRIMARY KEY (UUID)
) ENGINE=MyISAM; 
