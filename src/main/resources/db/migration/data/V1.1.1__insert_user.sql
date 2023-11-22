INSERT INTO public.roles (id, name)
VALUES ('e3d94e25-d118-4e9b-823c-72d407bf25b8', 'SUPER_ADMIN'),
        ('8b39760e-e6d7-489d-ae31-a020e6356c8c', 'ADMIN'),
        ('a4609d30-2a60-4bc5-9d5a-58c23ad753c2', 'USER');

INSERT INTO public.users (id, email, is_active, created_on)
VALUES ('1f6527e8-ac7b-4668-a6b5-3281df12f70c', 'quangndgcc200030@fpt.edu.vn', true, '2023-10-04 16:34:23.928');

INSERT INTO public.user_roles (user_id, role_id)
VALUES ('1f6527e8-ac7b-4668-a6b5-3281df12f70c', '8b39760e-e6d7-489d-ae31-a020e6356c8c'),
       ('1f6527e8-ac7b-4668-a6b5-3281df12f70c', 'a4609d30-2a60-4bc5-9d5a-58c23ad753c2')
