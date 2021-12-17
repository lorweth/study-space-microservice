package vn.vnedu.studyspace.group_store.service.factory;

import vn.vnedu.studyspace.group_store.domain.Group;

public class GroupFactory {

    public static Group getGroup(Long groupId){
        final Group group = new Group();
        group.setId(groupId);
        return group;
    }
}
