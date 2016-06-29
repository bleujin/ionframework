CREATE OR REPLACE TRIGGER SYNC_PUBLISH_CONTENT
AFTER INSERT OR UPDATE OR DELETE ON ARTICLE_TBLC
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
Declare v_action varchar2(10) := 'MOD';
BEGIN

    IF INSERTING Then
        v_action := 'INS' ;
    ElsIf DELETING Then
        v_action := 'DEL' ;
    ElsIf UPDATING Then
        v_action := 'MOD' ;
    End If ;

    IF (:new.isUsing = 'T' or :old.isUsing = 'T') Then
        If (v_action = 'MOD' and :new.catId != :old.catId) Then -- move content..
            Update article_search_status_tblc set action = 'DEL', modDate = sysdate
            Where catId = :old.catId and artId = :old.artId ;
        End If ;

        Merge into article_search_status_tblc d
        Using (
            Select NVL(:new.catId, :old.catId) catId, NVL(:new.artId, :old.artId) artId, v_action action
            From dual
            ) f
        On (d.catId = f.catId and d.artId = f.artId)
        When matched Then
            Update set d.action = f.action, d.modDate = sysdate
        When not matched Then
            Insert (d.catId, d.artId, d.action, isArticle)
            Values (f.catId, f.artId, f.action, 'F') ;

    End If ;

    --ibr_15453...
    if UPDATING then
        if (:old.isUsing = 'F' and :new.isUsing = 'T' ) Then
    		update category_article_tblc set status = Constants.PublishStatus, lastUpDate = sysdate Where artid = :new.artId ;
		End if ;

    elsif DELETING then
        delete from category_article_tblc where artId = :old.artId and :old.isUsing = 'T' ;

    end if ;

    -- ibr_17770
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            dbms_output.put_line(SQLERRM);

END
